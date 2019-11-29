package com.mapcalibration.mvvm

import com.mapcalibration.mvvm.data.remote.api.SearchAPI
import com.mapcalibration.mvvm.data.remote.response.RepositoriesResponse
import io.reactivex.Single

/**
 * @author Maciej Szreter
 */
class DummySearchAPI : SearchAPI {

    override fun search(params: MutableMap<String, String>): Single<RepositoriesResponse> {
        return Single.just(null)
    }

}