package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.here.android.mpa.common.*
import com.here.android.mpa.search.ErrorCode
import com.here.android.mpa.search.GeocodeRequest
import com.here.android.mpa.search.GeocodeResult
import com.here.android.mpa.search.ResultListener
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotoListAdapter
import com.openclassrooms.realestatemanager.utils.*
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
    private var photoFile: File? = null
    private var photos: ArrayList<Media.Photo> = arrayListOf()
    private var videos: ArrayList<Media.Video> = arrayListOf()
    private val args: AddPropertyFragmentArgs by navArgs()
    private var newPropertyId: Long = 0
    private lateinit var photoListAdapter: PhotoListAdapter
    private var address: String? = ""
    private var location: LatLng? = null
    private var latestTmpUri: Uri? = null
    private var feature: CarmenFeature? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var isConnected: Boolean = true

    private var address1: String? = ""
    private var address2: String? = ""
    private var city: String = "New York"
    private var zipCode: Int? = null
    private var state: String? = "NY"
    private var country: String = "United States"
    private var area: String? = ""
    private var lat: String? = null
    private var lng: String? = null


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
        if (::photoListAdapter.isInitialized) {
            photoListAdapter.submitList(list = photos)
        }
        isConnected = isNetworkConnected(requireContext())
        initMapEngine()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  this.binding.handlers = Handlers()
        photosRecyclerView = binding.media!!.photoRecyclerView
        binding.lifecycleOwner = this
        this.binding.viewModel = viewModel
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown
        setupMenuValues(typeDropdown)

        setFabListener()
        setupUploadImageListener()
        setupImageDialogListener()
        setupSellDateListener()
        retrieveArguments()
        setAddress()
    }

    private fun setAddress() {

        if (isConnected) {
            setAddressListener()
        } else {
            binding.address?.addressTextInput?.visibility = View.GONE
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
            //triggerGeocodeRequest()
        }
    }

    private fun triggerGeocodeRequest() {
        binding.address?.addressTextInput?.text.toString()
        /*
         * Create a GeocodeRequest object with the desired query string, then set the search area by
         * providing a GeoCoordinate and radius before executing the request.
         */
        // val query = "4350 Still Creek Dr,Burnaby"
        val query = "$address1, $city, $zipCode, $country"
        val geocodeRequest = GeocodeRequest(query)
        val coordinate = GeoCoordinate(49.266787, -123.056640)
        geocodeRequest.setSearchArea(coordinate, 5000)
        geocodeRequest.execute(object : ResultListener<List<GeocodeResult?>?> {
            override fun onCompleted(p0: List<GeocodeResult?>?, errorCode: ErrorCode?) {
                if (errorCode === ErrorCode.NONE) {
                    /*
                     * From the result object, we retrieve the location and its coordinate and
                     * display to the screen. Please refer to HERE Android SDK doc for other
                     * supported APIs.
                     */
                    val sb = StringBuilder()
                    if (p0 != null) {
                        for (result in p0) {
                            sb.append(result?.location!!.coordinate.toString())
                            sb.append("\n")
                            Timber.d("RESULT_HERE: ${result}")
                        }
                    }
                    updateTextView(sb.toString())
                } else {
                    updateTextView("ERROR:Geocode Request returned error code:$errorCode")
                }
            }

            private fun updateTextView(txt: String) {
                requireActivity().runOnUiThread(Runnable {
                    binding.address?.addressTextInput?.setText(
                        txt
                    )
                })
            }

        })
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
    ) {
        photosRecyclerView.apply {
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
        }
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
            navHostFragment.navigate(R.id.propertyListFragment)
            saveProperty()
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
        if (point.coordinates().size > 0)
            location = LatLng(point.coordinates()[0], point.coordinates()[1])
        val placeName = address?.split(",")
        if (placeName?.size!! > 2) {
            var placeCity = placeName.get(2).split(" ")
            binding.address?.cityTextInput?.setText(placeCity.get(0))
            binding.address?.zipcodeTextInput?.setText(placeCity.get(1))
        } else {
            binding.address?.cityTextInput?.setText(placeName.get(2))
            binding.address?.zipcodeTextInput?.setText(placeName.get(2))
        }
        binding.address?.address1TextInput?.setText(feature.text())
        binding.address?.stateTextInput?.setText(placeName.get(1))
        binding.address?.countryTextInput?.setText(placeName.get(3))

        Timber.d(
            "ADDRESS:  ${location}, context:  ${
                feature.context()?.map { it }?.filter { it.wikidata() contentEquals ("place") }
            }, placename= $placeName}"
        )

        //if ()
        // city = feature.context()
    }

    private fun saveProperty() {
        /* address1 = binding.address?.address1TextInput?.text.toString()
         address2 = binding.address?.address2TextInput?.text.toString()
         city = binding.address?.cityTextInput?.text.toString()
         zipCode = binding.address?.zipcodeTextInput?.text.toString().toInt()
         state = binding.address?.stateTextInput?.text.toString()
         country = binding.address?.countryTextInput?.text.toString()
         area = binding.address?.areaTextInput?.text.toString()*/

        val addPropertyView = AddPropertyViewModel.AddPropertyView(
            newPropertyId,
            binding.type!!.typeDropdown.text.toString(),
            binding.characteristics!!.priceTextInput.text.toString(),
            binding.characteristics!!.surfaceTextInput.text.toString(),
            binding.characteristics!!.numberOfRoomTextInput.text.toString(),
            binding.characteristics!!.numberOfBathroomTextInput.text.toString(),
            binding.characteristics!!.numberOfBedroomTextInput.text.toString(),
            binding.type!!.descriptionTextInput.text.toString(),
            binding.address?.address1TextInput?.text.toString(),
            binding.address?.address2TextInput?.text.toString(),
            binding.address?.cityTextInput?.text.toString(),
            binding.address?.zipcodeTextInput?.text.toString(),
            binding.address?.stateTextInput?.text.toString(),
            binding.address?.countryTextInput?.text.toString(),
            binding.address?.areaTextInput?.text.toString(),
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
        setupRecyclerView()
        photoListAdapter.submitList(photos)
        triggerGeocodeRequest()
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
        /*  val pickIntent =
              Intent(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
          startActivityForResult(pickIntent, REQUEST_GALLERY_IMAGE)*/
        selectImageFromGalleryResult.launch("image/*")
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


    private fun initMapEngine() {
        // This will use external storage to save map cache data, it is also possible to set
        // private app's path
        val path: String = File(requireActivity().getExternalFilesDir(null), ".here-map-data")
            .absolutePath
        // This method will throw IllegalArgumentException if provided path is not writable
        MapSettings.setDiskCacheRootPath(path)

        /*
         * Even though we don't display a map view in this application, in order to access any
         * services that HERE Android SDK provides, the MapEngine must be initialized as the
         * prerequisite.
         */MapEngine.getInstance().init(
            ApplicationContext(requireContext())
        ) { error ->
            if (error != OnEngineInitListener.Error.NONE) {
                AlertDialog.Builder(requireActivity()).setMessage(
                    """
                         Error : ${error.name}
                         
                         ${error.details}
                         """.trimIndent()
                )
                    .setTitle(R.string.engine_init_error)
                    .setNegativeButton(android.R.string.cancel,
                        DialogInterface.OnClickListener { dialog, which -> requireActivity().finish() })
                    .create().show()
            } else {
                Toast.makeText(
                    requireActivity(), "Map Engine initialized without error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}