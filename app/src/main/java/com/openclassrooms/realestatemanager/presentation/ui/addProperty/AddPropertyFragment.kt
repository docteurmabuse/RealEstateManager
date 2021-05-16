package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {
    private val viewModel: AddPropertyViewModel by viewModels()
    private var photoFile: File? = null
    private var photos: ArrayList<Media.Photo> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotosAdapter

    companion object {
        fun newInstance() = AddPropertyFragment()
        private const val REQUEST_CAPTURE_IMAGE = 1
        private const val REQUEST_PICK_IMAGE = 2

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
        val typeDropdown: AutoCompleteTextView = binding.type!!.typeDropdown
        recyclerView = binding.media!!.photoRecyclerView
        setupMenuValues(typeDropdown)
        //  this.binding.handlers = Handlers()
        binding.lifecycleOwner = this
        this.binding.viewModel = viewModel
        photos.add(
            Media.Photo(
                name = "living room",
                photoPath = "content://com.openclassrooms.realestatemanager.fileprovider/real_estate_manager_image/REM_20210516123152_2135246225477172142.jpg"
            )
        )

        setupRecyclerView()
        setFabListener()
        setupUploadImageListener()
        setupImageDialogListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val adapter = PhotosAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager
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
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.setAdapter(adapter)
    }

    private fun setFabListener() {
        binding.addPropertyFAB.setOnClickListener {
            setNewPropertyValues()
            //val navHostFragment = findNavController()
            //navHostFragment.navigate(R.id.propertyListFragment)
            //saveProperty()


        }
    }

    private fun setNewPropertyValues() {
        val type = binding.type!!.typeDropdown.text
        val price = binding.characteristics!!.priceTextInput.text.toString().toInt()
        val surface = binding.characteristics!!.surfaceTextInput.text.toString().toInt()
        val roomNumber = binding.characteristics!!.numberOfRoomTextInput.text.toString().toInt()
        val bathroomNumber =
            binding.characteristics!!.numberOfBathroomTextInput.text.toString().toInt()
        val bedroomNumber =
            binding.characteristics!!.numberOfBedroomTextInput.text.toString().toInt()
        val address = binding.address!!.addressTextInput.text.toString()
        val address2 = binding.address!!.address2TextInput.text.toString()
        val city = binding.address!!.cityTextInput.text.toString()
        val state = binding.address!!.stateTextInput.text.toString()
        val zipcode = binding.address!!.zipcodeTextInput.text.toString().toInt()
        val country = binding.address!!.countryTextInput.text.toString()
        val museum = binding.pointOfInterest!!.museum.isChecked
        val schools = binding.pointOfInterest!!.schools.isChecked
        val shops = binding.pointOfInterest!!.shops.isChecked
        val hospital = binding.pointOfInterest!!.hospital.isChecked
        val station = binding.pointOfInterest!!.station.isChecked
        val parcs = binding.pointOfInterest!!.parcs.isChecked

        Timber.tag("FabClick")
            .d("It's ok FABSAVE: ${type}, $price, $surface, $roomNumber, $bathroomNumber, $bedroomNumber")
    }

    fun saveProperty() {
        Timber.tag("FabClick").d("It's ok FABSAVE: ${binding.typeDropdown!!.text}")
        //addPropertyViewModel.addPropertyToRoomDb()
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
                    val photo = Media.Photo("", uri.toString())
                    submitPhotoToList(photo)

                    // val image = getImageWithPath(photoFile.absolutePath)
                }
            }
        }
    }

    private fun submitPhotoToList(photo: Media.Photo) {
        photos.add(photo)
        Timber.d("PHOTOS: ${photos[0]}")
        val adapter = PhotosAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(photos)
    }

    private fun onPickClick() {
        Toast.makeText(activity, "Camera Pick", Toast.LENGTH_SHORT).show()

    }
}