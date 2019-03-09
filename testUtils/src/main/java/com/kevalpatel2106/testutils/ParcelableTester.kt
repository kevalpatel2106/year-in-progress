package com.kevalpatel2106.testutils

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * From https://gist.github.com/tomaszpolanski/92a2eada1e06e4a4c71abb298d397173#file-utils-kt
 */

inline fun <reified R : Parcelable> R.testParcel(): R {
    val bytes = marshallParcelable(this)
    return unmarshallParcelable(bytes)
}

inline fun <reified R : Parcelable> marshallParcelable(parcelable: R): ByteArray {
    val bundle = Bundle().apply { putParcelable(R::class.java.name, parcelable) }
    return marshall(bundle)
}

@SuppressLint("Recycle")
fun marshall(bundle: Bundle): ByteArray =
    Parcel.obtain().use {
        it.writeBundle(bundle)
        it.marshall()
    }

@SuppressLint("ParcelClassLoader")
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun <reified R : Parcelable> unmarshallParcelable(bytes: ByteArray): R = unmarshall(bytes)
    .readBundle()
    .run {
        classLoader = R::class.java.classLoader
        getParcelable(R::class.java.name)
    }

@SuppressLint("Recycle")
fun unmarshall(bytes: ByteArray): Parcel =
    Parcel.obtain().apply {
        unmarshall(bytes, 0, bytes.size)
        setDataPosition(0)
    }

private fun <T> Parcel.use(block: (Parcel) -> T): T =
    try {
        block(this)
    } finally {
        this.recycle()
    }