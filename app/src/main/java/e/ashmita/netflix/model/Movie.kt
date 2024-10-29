package e.ashmita.netflix.model

import java.io.Serializable

public class Movie(

    val videoUrl: String? = null,
    val movieImage: String? = null,
    val movieName: String? = null,
    val movieCategory: String? = null,
    val movieRating: String? = null,
    val language: String? = null,
    val developer: String? = null,
    val releaseYear: String? = null
) : Serializable

//): Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(videoUrl)
//        parcel.writeString(movieImage)
//        parcel.writeString(movieName)
//        parcel.writeString(movieCategory)
//        parcel.writeString(movieRating)
//        parcel.writeString(language)
//        parcel.writeString(developer)
//        parcel.writeString(releaseYear)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Movie> {
//        override fun createFromParcel(parcel: Parcel): Movie {
//            return Movie(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Movie?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
