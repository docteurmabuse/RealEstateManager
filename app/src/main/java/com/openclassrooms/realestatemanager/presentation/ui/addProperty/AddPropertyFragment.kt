package com.openclassrooms.realestatemanager.presentation.ui.addProperty

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
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.AddPropertyFragmentBinding
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PhotosAdapter
import com.openclassrooms.realestatemanager.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
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


    companion object {
        fun newInstance() = AddPropertyFragment()
        private const val REQUEST_CAPTURE_IMAGE = 1
        private const val REQUEST_GALLERY_IMAGE = 2

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

        setupRecyclerView()
        setFabListener()
        setupUploadImageListener()
        setupImageDialogListener()
        setupSellDateListener()
        retrieveArguments()
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

    private fun setupRecyclerView() {
        val adapter = PhotosAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(photos)
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
            binding.address!!.addressTextInput.text.toString(),
            binding.address!!.address2TextInput.text.toString(),
            binding.address!!.cityTextInput.text.toString(),
            binding.address!!.zipcodeTextInput.text.toString(),
            binding.address!!.stateTextInput.text.toString(),
            binding.address!!.countryTextInput.text.toString(),
            binding.address!!.areaTextInput.text.toString(),
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == android.app.Activity.RESULT_OK) {
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
        val adapter = PhotosAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(photos)
    }

    private fun onPickClick() {
        val pickIntent =
            Intent(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        startActivityForResult(pickIntent, REQUEST_GALLERY_IMAGE)
    }

    override fun onDestroyView() {
        _binding = null
        recyclerView.adapter = null
        super.onDestroyView()

    }
}