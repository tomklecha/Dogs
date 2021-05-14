package com.tkdev.dogs.data

import com.tkdev.dogs.R
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.data.model.ApiResponse
import com.tkdev.dogs.data.model.BreedDomain
import com.tkdev.dogs.data.model.DogModel
import java.util.*
import kotlin.random.Random

interface DogsApiMapper {
    fun mapDogsList(dogs: Map<String, List<String>>): ApiResponse<List<DogModel>>
    fun mapPicturesWithBreed(
        breedDomain: BreedDomain,
        dogModel: DogModel
    ): ApiResponse<List<String>>
}

class DogsApiMapperDefault(private val stringWrapper: StringWrapper) : DogsApiMapper {
    override fun mapDogsList(dogs: Map<String, List<String>>): ApiResponse<List<DogModel>> {
        val list = mutableListOf<DogModel>()
        var id = 0
        dogs.map { dog ->
            val dogBreedList = dog.value
            when (dogBreedList.isNotEmpty()) {
                true -> {
                    dogBreedList.forEach { dogWithBreed ->
                        list.add(
                            DogModel(
                                id,
                                dog.key,
                                dogWithBreed,
                                "${dogWithBreed.capitalize(Locale.getDefault())} ${dog.key.capitalize(Locale.getDefault())}"
                            )
                        )
                        id++
                    }
                }
                false -> {
                    list.add(
                        DogModel(
                            id,
                            dog.key,
                            "",
                            dog.key.capitalize(Locale.getDefault())
                        )
                    )
                    id++
                }
            }
        }
        return when (list.isEmpty()) {
            true -> ApiResponse.Fail(stringWrapper.getString(R.string.mapper_list_empty), list)
            false -> ApiResponse.Success(list.toList())
        }
    }

    override fun mapPicturesWithBreed(
        breedDomain: BreedDomain,
        dogModel: DogModel
    ): ApiResponse<List<String>> {
        var list = breedDomain.message.toMutableList()
        if (dogModel.dogSubBreed.isNotEmpty()) {
            list.removeAll { image -> !image.contains(dogModel.dogSubBreed) }
        }
        if (list.size > 10) {
            val reducedList = mutableListOf<String>()
            for (index in 0..9) {
                val listIndex = Random.nextInt(0, list.size - 1)
                reducedList.add(list[listIndex])
                list.removeAt(listIndex)
            }
            list = reducedList
        }
        return when (list.isEmpty()) {
            true -> ApiResponse.Fail(stringWrapper.getString(R.string.mapper_list_empty), list)
            false -> ApiResponse.Success(list)
        }
    }

}
