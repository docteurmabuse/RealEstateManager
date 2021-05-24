package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotosAdapter
import com.openclassrooms.realestatemanager.utils.ImageUtils
import com.openclassrooms.realestatemanager.utils.REQUEST_CODE_AUTOCOMPLETE
import com.openclassrooms.realestatemanager.utils.Utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.*

@AndroidEntryPoint
class AddPropertyFragment : androidx.fragment.app.Fragment(R.layout.add_property_fragment) {
    private val viewModel: AddPropertyViewModel by viewModels()
    private var photoFile: File? = null
    private var photos: ArrayList<Media.Photo> = arrayListOf()
    private var videos: ArrayList<Media.Video> = arrayListOf()
    private val args: AddPropertyFragmentArgs by navArgs()
    private var newPropertyId: Long = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotosAdapter
    private var address: String? = ""
    private var location: LatLng? = null
    private var latestTmpUri: Uri? = null
    private var feature: CarmenFeature? = null
    var address1: String? = ""
    var address2: String? = ""
    var city: String = "New York"
    var zipCode: Int? = null
    var state: String? = "NY"
    var country: String = "United States"
    var area: String? = null
    var lat: String? = null
    var lng: String? = null

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private var _binding: AddPropertyFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPropertyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.media!!.photoRecyclerView
        //  this.binding.handlers = Handlers()
        binding.lifecycleOwner = this
        this.binding.viewModel = viewModel
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown
        setupMenuValues(typeDropdown)
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Media.Photo
            photos.remove(item)
            adapter.submitList(photos)
            Timber.d("PHOTO_DELETE: ${item.photoPath}")
        }
        setupRecyclerView(recyclerView, onClickListener)
        setFabListener()
        setupUploadImageListener()
        setupImageDialogListener()
        setupSellDateListener()
        retrieveArguments()
        setAddressListener()
        setObserver()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            val value = viewModel.statePhotos
            value.collect {
                setPhotosAdapter(it)
                Timber.d("PHOTO_OBSERVER: $it")
            }
        }
    }

    private fun setPhotosAdapter(photoList: List<Media.Photo>) {
        photos.addAll(photoList)
        adapter.submitList(photos)
    }

    private fun retrieveArguments() {
        newPropertyId = args.propertyId
        Timber.d("ADDPROPERTY: ${newPropertyId}")
    }

    private fun setupSellDateListener() {
        binding.dates!!.sellDateDropdown.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            var date: Long = cldr.timeInMillis
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    binding.dates!!.sellDateDropdown.setText(
                        "$dayOfMonth / $monthOfYear + 1)/ $year"
                    )
                },
                year,
                month,
                day
            )
            picker.show()
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
    ) {
        adapter = PhotosAdapter(onClickListener)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupImageDialogListener() {
        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getString("bundleKey")
            when (result) {
                "capture" -> onCaptureClick()
                "pick" -> onPickClick()
                else -> {
                    Timber.e("Something went wrong")
                }
            }
        }
    }

    private fun setupUploadImageListener() {
        binding.media!!.buttonPhoto.setOnClickListener {
            addImageToRecyclerView()
        }
    }

    private fun addImageToRecyclerView() {
        val newFragment = PhotoOptionDialogFragment.newInstance(this.requireContext())
        newFragment?.show(childFragmentManager, "photoOptionDialog")
    }

    private fun setupMenuValues(dropdown: AutoCompleteTextView) {
        val items = Property.PropertyType.values()
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                items
            )
        dropdown.setAdapter(adapter)
    }

    private fun setFabListener() {
        binding.addPropertyFAB.setOnClickListener {
            val navHostFragment = findNavController()
            navHostFragment.navigate(R.id.propertyListFragment)
            saveProperty()
        }
    }

    private fun setAddressListener() {
        binding.address?.addressTextInput?.setOnClickListener {
            if (isNetworkConnected(requireContext())) {
                Timber.d("INTERNET_CONNECTION: Internet is connected")
            } else {
                Timber.d("INTERNET_CONNECTION: Internet is not connected")
            }
            popupAutocomplete(it)
        }
    }


    private lateinit var intent: Intent
    private var placeOptions: PlaceOptions? = null
    private fun popupAutocomplete(it: View?) {
        intent = PlaceAutocomplete.IntentBuilder()
            .accessToken(
                getString(R.string.mapbox_access_token)
            )
            .placeOptions(
                PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#FFFFFF"))
                    .limit(5)
                    .geocodingTypes("address")
                    .country(Locale.US)
                    .build()
            )
            .build(requireActivity())
        Timber.tag("AddressClick").d("It's ok Addresspop")
        startActivityForResult(
            intent, REQUEST_CODE_AUTOCOMPLETE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val feature = PlaceAutocomplete.getPlace(data)
            submitAddress(feature)
        }
    }

    private fun submitAddress(feature: CarmenFeature) {
        address = feature.placeName()
        binding.address?.addressTextInput?.setText(address)
        val point: Point = feature.geometry() as Point
        location = LatLng(point.coordinates()[0], point.coordinates()[1])

        var placename = address?.split(",")

        Timber.d(
            "ADDRESS:  ${location}, context:  ${
                listOf(
                    feature.context()?.component2()
                )[1]
            }, placename= $placename"
        )

        //if ()
        // city = feature.context()
    }

    private fun saveProperty() {
        val addPropertyView = AddPropertyViewModel.AddPropertyView(
            newPropertyId,
            binding.type!!.typeDropdown.text.toString(),
            binding.characteristics!!.priceTextInput.text.toString(),
            binding.characteristics!!.surfaceTextInput.text.toString(),
            binding.characteristics!!.numberOfRoomTextInput.text.toString(),
            binding.characteristics!!.numberOfBathroomTextInput.text.toString(),
            binding.characteristics!!.numberOfBedroomTextInput.text.toString(),
            binding.type!!.descriptionTextInput.text.toString(),
            address1,
            binding.address?.address2TextInput?.text.toString(),
            city,
            zipCode.toString(),
            state,
            country,
            area,
            binding.pointOfInterest!!.museum.isChecked,
            binding.pointOfInterest!!.schools.isChecked,
            binding.pointOfInterest!!.shops.isChecked,
            binding.pointOfInterest!!.hospital.isChecked,
            binding.pointOfInterest!!.station.isChecked,
            binding.pointOfInterest!!.parcs.isChecked,
            binding.dates!!.soldSwitch.isChecked,
            //DateUtil.stringToDate(binding.dates!!.sellDateDropdown.text.toString()),
            //  DateUtil.stringToDate(binding.dates!!.soldDateDropdown.text.toString()),
            null, null,
            Media(photos, videos),
            "Maurice Chevalier"
        )
        Timber.tag("FabClick")
            .d("It's ok FABSAVE: ${addPropertyView.type}, $addPropertyView!!.price, $addPropertyView!!.surface, $addPropertyView!!.roomNumber, $addPropertyView!!.bathroomNumber, $addPropertyView!!.bedroomNumber")

        viewModel.saveProperty(addPropertyView)
    }

    class Handlers {
        fun onClickFriend(property: String) {
            Timber.tag("FabClick").d("It's ok FAB Handler:$property")
            //private val viewModel: AddPropertyViewModel by viewModels()

        }
    }

    private fun onCaptureClick() {
        /* photoFile = null
         try {
             photoFile = ImageUtils.createUniqueImageFile(requireContext())
         } catch (ex: java.io.IOException) {
             return
         }
         photoFile?.let { photoFile ->
             val photoUri = FileProvider.getUriForFile(
                 requireActivity(), "com.openclassrooms.realestatemanager.fileprovider",
                 photoFile
             )
             val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
             captureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
             val intentActivities = requireActivity().packageManager.queryIntentActivities(
                 captureIntent, PackageManager.MATCH_DEFAULT_ONLY
             )
             intentActivities.map {
                 it.activityInfo.packageName
             }
                 .forEach {
                     requireActivity().grantUriPermission(
                         it,
                         photoUri,
                         Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                     )
                 }
             startActivityForResult(captureIntent, REQUEST_CAPTURE_IMAGE)
         }*/

        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext(),
            "com.openclassrooms.realestatemanager.fileprovider",
            tmpFile
        )
    }

/* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
     if (resultCode == Activity.RESULT_OK) {
         when (requestCode) {
             REQUEST_CAPTURE_IMAGE -> {
                 val photoFile = photoFile ?: return
                 val uri = FileProvider.getUriForFile(
                     requireContext(),
                     "com.openclassrooms.realestatemanager.fileprovider",
                     photoFile
                 )
                 requireContext().revokeUriPermission(
                     uri,
                     Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                 )
                 val image = getImageWithPath(photoFile.absolutePath)
                 val file = File(photoFile.absolutePath)
                 MediaScannerConnection.scanFile(
                     context, arrayOf(file.toString()),
                     null, null
                 )
                 val photo = Media.Photo(
                     "",
                     uri.toString()
                 )
                 submitPhotoToList(photo)
             }
             REQUEST_GALLERY_IMAGE -> if (data != null && data.data != null) {
                 val imageUri = data.data
                 val photo = Media.Photo(
                     "",
                     imageUri.toString()
                 )
                 submitPhotoToList(photo)
             }
             REQUEST_CODE_AUTOCOMPLETE -> if (data != null && data.data != null) {
                 val feature = PlaceAutocomplete.getPlace(data)
                 Timber.d("ADDRESS: ${feature.address()}, ${feature.geometry()}")
                 submitAddress(feature)
                 Toast.makeText(requireContext(), feature.text(), Toast.LENGTH_LONG).show()
             }
         }
     }

 }*/


    private fun getImageWithAuthority(uri: Uri) = ImageUtils.decodeUriStreamToSize(
        uri,
        resources.getDimensionPixelSize(R.dimen.default_image_width),
        resources.getDimensionPixelSize(R.dimen.default_image_height),
        requireContext()
    )

    private fun getImageWithPath(filePath: String) = ImageUtils.decodeFileToSize(
        filePath,
        resources.getDimensionPixelSize(R.dimen.default_image_width),
        resources.getDimensionPixelSize(R.dimen.default_image_height)
    )

    private fun submitPhotoToList(photo: Media.Photo) {
        viewModel.addPhotoToPhotosList(photo)
        Timber.d("PHOTOS: ${photo.photoPath}")
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val photo = Media.Photo(
                    "",
                    uri.toString()
                )
                submitPhotoToList(photo)
                setObserver()
            }
        }
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    val photo = Media.Photo(
                        "",
                        uri.toString()
                    )
                    submitPhotoToList(photo)
                }
            }
        }

    private fun onPickClick() {
        /*  val pickIntent =
              Intent(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
          startActivityForResult(pickIntent, REQUEST_GALLERY_IMAGE)*/
        selectImageFromGalleryResult.launch("image/*")
    }

    override fun onDestroyView() {
        _binding = null
        recyclerView.adapter = null
        super.onDestroyView()
    }
}