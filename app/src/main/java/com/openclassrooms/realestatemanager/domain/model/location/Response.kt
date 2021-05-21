package com.openclassrooms.realestatemanager.domain.model.location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import  kotlinx.parcelize.Parcelize

@Parcelize
data class Response(

	@field:SerializedName("place_name")
	val placeName: String? = null,

	@field:SerializedName("center")
	val center: List<Double?>? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("language_fr-FR")
	val languageFrFR: String? = null,

	@field:SerializedName("relevance")
	val relevance: Double? = null,

	@field:SerializedName("text_fr-FR")
	val textFrFR: String? = null,

	@field:SerializedName("place_type")
	val placeType: List<String?>? = null,

	@field:SerializedName("context")
	val context: List<ContextItem?>? = null,

	@field:SerializedName("place_name_fr-FR")
	val placeNameFrFR: String? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("properties")
	val properties: Properties? = null
) : Parcelable

@Parcelize
data class Properties(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("foursquare")
	val foursquare: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("landmark")
	val landmark: Boolean? = null,

	@field:SerializedName("wikidata")
	val wikidata: String? = null
) : Parcelable

@Parcelize
data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Double?>? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable

@Parcelize
data class ContextItem(

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("language_fr-FR")
	val languageFrFR: String? = null,

	@field:SerializedName("wikidata")
	val wikidata: String? = null,

	@field:SerializedName("text_fr-FR")
	val textFrFR: String? = null,

	@field:SerializedName("short_code")
	val shortCode: String? = null
) : Parcelable
