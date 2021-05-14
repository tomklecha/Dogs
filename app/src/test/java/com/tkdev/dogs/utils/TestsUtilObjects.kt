package com.tkdev.dogs.utils

import com.tkdev.dogs.data.model.BreedDomain
import com.tkdev.dogs.data.model.DogModel
import kotlin.random.Random

val testID = Random.nextInt()
val testBreed = "that is a"
val testSubBreed = "goodboy"
val testDisplayName = "${testSubBreed.capitalize()} ${testBreed.capitalize()}"

val testDogModel = DogModel(testID, testBreed, testSubBreed, testDisplayName)
val testListDogModel = listOf(testDogModel)

val testPictureUrl = "www.whoisgoodboy.woof"
val testListPictureUrl = listOf(testPictureUrl)

val testDogMap: Map<String, List<String>> = mapOf(testBreed to listOf())
val testDogMapWithSubBreed: Map<String, List<String>> = mapOf(testBreed to listOf(testSubBreed))

val testlistPictureUrl = listOf(testPictureUrl)
val testlistTenPictureUrl = listOf(
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
    testPictureUrl,
)
val testBreedDomain = BreedDomain(testlistPictureUrl, "status")
val testBreedDomainEmpty = BreedDomain(listOf(), "status")
val testBreedDomainAboveTenElements = BreedDomain(testlistTenPictureUrl, "status")

