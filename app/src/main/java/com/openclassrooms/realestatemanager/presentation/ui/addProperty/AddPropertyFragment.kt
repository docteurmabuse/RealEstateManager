package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotoListAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragmentArgs
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.utils.DateUtil.dateToString
import com.openclassrooms.realestatemanager.utils.DateUtil.getDate
import com.openclassrooms.realestatemanager.utils.DateUtil.longToDate
import com.openclassrooms.realestatemanager.utils.Utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.*

private const val REQ_CAPTURE = 100
private const val RES_IMAGE = 100

@AndroidEntryPoint
class AddPropertyFragment : androidx.fragment.app.Fragment(R.layout.add_property_fragment),
    PhotoListAdapter.Interaction, OnStartDragListener {
    private val viewModel: AddPropertyViewModel by viewModels()
    private lateinit var photosRecyclerView: RecyclerView
    private val permissions = arrayOf(Manifest.permission.CAMERA)


    //Nav Arguments
    private val args: AddPropertyFragmentArgs by navArgs()
    private var newPropertyId: Long = 0
    private var property: Property? = null
    private var isEditPropertyView: Boolean = false

    //Adapter
    private lateinit var photoListAdapter: PhotoListAdapter

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

    private var queryImageUrl: String = ""
    private var imgPath: String = ""
    private var imageUri: Uri? = null
    private var isPermissionsAllowed: Boolean = false

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
        photosRecyclerView = binding.media!!.photoRecyclerView

        if (::photoListAdapter.isInitialized) {
            photoListAdapter.submitList(list = photos)
        }
        isConnected = isNetworkConnected(requireContext())
        retrievedArguments()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  this.binding.handlers = Handlers()
        binding.lifecycleOwner = this
        this.binding.viewModel = viewModel
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown
        setUpPermissions()
        setupMenuValues(typeDropdown)
        setFabListener()
        setupSellDateListener()
        retrieveArguments()
        setPhotosObserver()
        setupUploadImageListener()
    }

    private fun setUpPermissions() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            Timber.d("PERMISSIONS OK")
                            isPermissionsAllowed = true
                        } else {
                            isPermissionsAllowed = false
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Timber.d(it.name)
            }
            .check()

    }


    override fun onResume() {
        super.onResume()
        setPhotosObserver()
    }


    private fun retrievedArguments() {
        val bundle = arguments
        if (bundle == null) {
            Timber.d("PropertyDetailFragment did not received arguments")
            return
        }
        val args = PropertyDetailFragmentArgs.fromBundle(bundle)
        property = args.property
        isEditPropertyView = args.editPropertyView
        property?.let { setPropertyInLayout(it) }
    }

    private fun setPropertyInLayout(property: Property) {
        Timber.d("PROPERTY_DETAIL layout: $property")

        binding.property = property
        photos = property.media.photos as ArrayList<Media.Photo>
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
        }
        viewModel.initPhotosList(photos)

        setupRecyclerView()
        setPhotosObserver()
    }

    private fun retrieveArguments() {
        newPropertyId = args.propertyId
        Timber.d("ADDPROPERTY: ${newPropertyId}")
    }

    private fun setPhotosObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            whenStarted {
                val value = viewModel.statePhotos
                value.collect {
                    photoList(it as ArrayList<Media.Photo>)
                    Timber.d("PHOTOS_VIEWMODEL: ${photos}")
                }
            }
        }
    }

    private fun photoList(photoList: ArrayList<Media.Photo>) {
        Timber.d("PHOTOS_VIEWMODEL1: ${this.photos}")
        this.photos.clear()
        this.photos.addAll(photoList)
        // photos.addAll(photoList)
        Timber.d("PHOTOS_VIEWMODEL2: ${photos}")
        setupRecyclerView()
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


    private fun setupRecyclerView() {
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



    private fun setupUploadImageListener() {
        binding.media!!.buttonPhoto.setOnClickListener {
            chooseImage()
            if (isPermissionsAllowed) {
                chooseImage()
            }
        }
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
            if (isEditPropertyView) {
                updateProperty()
            } else {
                saveProperty()
            }
            val action =
                AddPropertyFragmentDirections.actionAddPropertyFragmentToItemTabsFragment2()
            navHostFragment.navigate(action)
            // navHostFragment.navigate(R.id.propertyListFragment)
        }
    }

    private fun updateProperty() {
        property?.let { viewModel.updatePropertyToRoomDb(it) }
    }


    private lateinit var intent: Intent


    private fun saveProperty() {

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
        }
    }


    private fun submitPhotoToList(photo: Media.Photo) {
        //photos.add(photo)
        Timber.d("PHOTOS: ${photo.photoPath}")
        viewModel.addPhotoToPhotosList(photo)
        setPhotosObserver()
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
        //  Intent(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            // startActivityForResult(pickIntent, REQUEST_GALLERY_IMAGE)
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_CAPTURE -> {
                if (isPermissionsAllowed) {
                    chooseImage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_not_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RES_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleImageRequest(data)
                }
            }
        }
    }

    fun chooseImage() {
        startActivityForResult(
            getPickImageIntent(),
            RES_IMAGE
        )
    }

    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())

        intentList = addIntentsToList(requireContext(), intentList, pickIntent)
        intentList = addIntentsToList(requireContext(), intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                getString(R.string.select_capture_image)
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun setImageUri(): Uri {
        val folder = File("${requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + getString(R.string.file_provider_name),
            file
        )
        imgPath = file.absolutePath
        return imageUri!!
    }


    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    private fun handleImageRequest(data: Intent?) {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            t.printStackTrace()
            //      progressBar.visibility = View.GONE
            Toast.makeText(
                requireContext(),
                t.localizedMessage ?: getString(R.string.some_err),
                Toast.LENGTH_SHORT
            ).show()
        }

        lifecycleScope.launch(Dispatchers.Main + exceptionHandler) {
            //progressBar.visibility = View.VISIBLE

            if (data?.data != null) {     //Photo from gallery
                imageUri = data.data
                queryImageUrl = imageUri?.path!!
                queryImageUrl =
                    requireActivity().compressImageFile(queryImageUrl, false, imageUri!!)
            } else {
                queryImageUrl = imgPath
                requireActivity().compressImageFile(queryImageUrl, uri = imageUri!!)
            }
            imageUri = Uri.fromFile(File(queryImageUrl))

            if (queryImageUrl.isNotEmpty()) {

                submitPhotoToList(Media.Photo("", imageUri.toString()))

            }
        }

    }
}
