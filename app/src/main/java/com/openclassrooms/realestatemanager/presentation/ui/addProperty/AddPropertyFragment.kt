package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotoListAdapter
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotosAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragmentArgs
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.utils.DateUtil.dateToString
import com.openclassrooms.realestatemanager.utils.DateUtil.getDate
import com.openclassrooms.realestatemanager.utils.DateUtil.longToDate
import com.openclassrooms.realestatemanager.utils.Utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*


@AndroidEntryPoint
class AddPropertyFragment : androidx.fragment.app.Fragment(R.layout.add_property_fragment),
    PhotoListAdapter.Interaction, OnStartDragListener {
    private val viewModel: AddPropertyViewModel by viewModels()
    private lateinit var photosRecyclerView: RecyclerView


    //Nav Arguments
    private val args: AddPropertyFragmentArgs by navArgs()
    private var newPropertyId: Long = 0
    private var property: Property? = null
    private var isEditPropertyView: Boolean = false

    //Adapter
    private lateinit var photoListAdapter: PhotosAdapter

    //Arguments
    private var photoFile: File? = null
    private var photos: ArrayList<Media.Photo> = arrayListOf()
    private var videos: ArrayList<Media.Video> = arrayListOf()
    private var address: String? = ""
    private var location: LatLng? = null
    private var latestTmpUri: Uri? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var isConnected: Boolean = true
    private var address1: String? = ""
    private var address2: String? = ""
    private var city: String = "New York"
    private var zipCode: Int? = null
    private var state: String? = "NY"
    private var country: String = "United States"
    private var area: String? = ""
    private var lat: Double? = null
    private var lng: Double? = null


    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private var _binding: AddPropertyFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPropertyFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = viewModel
            property = property
        }
        retrievesArguments()
        isConnected = isNetworkConnected(requireContext())
        binding.lifecycleOwner = this.viewLifecycleOwner
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown

        setupMenuValues(typeDropdown)
        setFabListener()
        setupUploadImageListener()
        setupImageDialogListener()
        setupSellDateListener()
        retrievesArguments()
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  this.binding.handlers = Handlers()
        photosRecyclerView = binding.media!!.photosRecyclerView
        binding.lifecycleOwner = this

    }

    private fun retrievesArguments() {
        val bundle = arguments
        if (bundle == null) {
            Timber.d("PropertyDetailFragment did not received arguments")
            return
        }
        newPropertyId = args.propertyId
        val args = PropertyDetailFragmentArgs.fromBundle(bundle)
        property = args.property
        isEditPropertyView = args.editPropertyView
        property?.let { setPropertyInLayout(it) }
        photos = property?.media?.photos as ArrayList<Media.Photo>
    }

    private fun setPropertyInLayout(property: Property) {
        Timber.d("PROPERTY_DETAIL layout: $property")

        binding.property = property
        binding.dates?.soldSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.dates!!.property?.sold = isChecked
            binding.dates!!.soldInputLayout.isVisible = isChecked
            Timber.d("SWITCH: $isChecked")
        }
        if (!EDIT_PROPERTY_VIEW) {
            binding.dates!!.soldInputLayout.visibility = View.GONE
            binding.dates!!.soldDateDropdown.visibility = View.GONE
            binding.dates!!.switchTitle.visibility = View.GONE
            binding.dates!!.soldSwitch.visibility = View.GONE
        } else {

        }
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
                { _, year, monthOfYear, dayOfMonth ->
                },
                year,
                month,
                day
            )
            picker.show()

            val dateOnMarket: Date = picker.datePicker.getDate()
            binding.dates!!.property?.sellDate = dateOnMarket
            binding.dates!!.sellDateDropdown.setText(longToDate(date)?.let { it1 -> dateToString(it1) })

            Timber.d("DATE_PICKER : $date, {${longToDate(date)}}")
        }
    }


    private fun setupRecyclerView(
    ) {
        if (property != null) {
            photoListAdapter = PhotosAdapter()
            binding.media?.photosRecyclerView?.adapter = photoListAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
        /*   photosRecyclerView.apply {
               layoutManager = LinearLayoutManager(activity)
               val topSpacingItemDecoration = TopSpacingItemDecoration(5)
               addItemDecoration(topSpacingItemDecoration)
               photoListAdapter = PhotoListAdapter(
                   this@AddPropertyFragment,
                   dragStartListener = this@AddPropertyFragment
               ) {
                   Timber.d("REORDER: reorder completed")
                   photoListAdapter.submitList(photos)
               }
               binding.media?.photoRecyclerView?.adapter = photoListAdapter
               val callback: ItemTouchHelper.Callback = ReorderHelperCallback(photoListAdapter)
               mItemTouchHelper = ItemTouchHelper(callback)
               mItemTouchHelper?.attachToRecyclerView(binding.media?.photoRecyclerView)
               photoListAdapter.submitList(photos)
           }*/
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            mItemTouchHelper?.startDrag(it)
        }
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
        val dropdownAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                items
            )
        dropdown.setAdapter(dropdownAdapter)
    }

    private fun setFabListener() {
        binding.addPropertyFAB.setOnClickListener {
            val navHostFragment = findNavController()
            saveProperty()
            val action =
                AddPropertyFragmentDirections.actionAddPropertyFragmentToItemTabsFragment2()
            navHostFragment.navigate(action)
            // navHostFragment.navigate(R.id.propertyListFragment)
        }
    }


    private lateinit var intent: Intent


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

            }
        }

    }


    private fun saveProperty() {
        /* address1 = binding.address?.address1TextInput?.text.toString()
         address2 = binding.address?.address2TextInput?.text.toString()
         city = binding.address?.cityTextInput?.text.toString()
         zipCode = binding.address?.zipcodeTextInput?.text.toString().toInt()
         state = binding.address?.stateTextInput?.text.toString()
         country = binding.address?.countryTextInput?.text.toString()
         area = binding.address?.areaTextInput?.text.toString()*/
        if (location != null) {
            lat = location!!.latitude
            lng = location!!.longitude
        } else {
            lat = 0.0
            lng = 0.0
        }
        zipCode = if (binding.address?.zipcodeTextInput?.text?.toString()?.toIntOrNull() != null) {
            binding.address?.zipcodeTextInput?.text.toString().toInt()
        } else {
            10000
        }
        val address = Address(
            address1 = binding.address?.address1TextInput?.text.toString(),
            address2 = binding.address?.address2TextInput?.text.toString(),
            city = binding.address?.cityTextInput?.text.toString(),
            zipCode,
            state = binding.address?.stateTextInput?.text.toString(),
            country = binding.address?.countryTextInput?.text.toString(),
            area = binding.address?.areaTextInput?.text.toString(),
            lat,
            lng
        )
        val addPropertyView = AddPropertyViewModel.AddPropertyView(
            newPropertyId,
            binding.type!!.typeDropdown.text.toString(),
            binding.price!!.priceTextInput.text.toString(),
            binding.characteristics!!.surfaceTextInput.text.toString(),
            binding.characteristics!!.numberOfRoomTextInput.text.toString(),
            binding.characteristics!!.numberOfBathroomTextInput.text.toString(),
            binding.characteristics!!.numberOfBedroomTextInput.text.toString(),
            binding.description!!.descriptionTextInput.text.toString(),
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
            "Maurice Chevalier",
            address
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
        photoFile = null
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
        }


        /* lifecycleScope.launchWhenStarted {
             getTmpFileUri().let { uri ->
                 latestTmpUri = uri
                 takeImageResult.launch(uri)
             }
         }*/
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
        photos.add(photo)
        Timber.d("PHOTOS: ${photo.photoPath}")
        if (property != null) {
            photoListAdapter = PhotosAdapter()
            binding.media?.photosRecyclerView?.adapter = photoListAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
        photoListAdapter.submitList(photos)
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val photo = Media.Photo(
                    "",
                    uri.toString()
                )
                submitPhotoToList(photo)
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
        val pickIntent =
            Intent(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        startActivityForResult(pickIntent, REQUEST_GALLERY_IMAGE)
        //selectImageFromGalleryResult.launch("image/*")
    }

    override fun onDestroyView() {
        _binding = null
        //binding.media!!.photoRecyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onItemSelected(position: Int, item: Media.Photo) {
        photos.remove(item)
        Timber.d("PHOTO_DELETE: ${item.photoPath}, newlist = $photos")
        setupRecyclerView()
    }

    /*private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                viewHolder1: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.absoluteAdapterPosition
                photos.removeAt(position)
                photosRecyclerView.adapter?.notifyItemRemoved(position)
            }
        }
    }*/

}
