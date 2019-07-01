package com.example.android.footballapp

import com.example.android.footballapp.api.ApiRepository
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ApiRepositoryTest {

    @Test
    fun apiRepository() {
        val apiRepository = mock(ApiRepository::class.java)
        // mencoba request data dari api menggunakan salah satu id dari liga German
        val url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4331"
        val url2 = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4331"
        apiRepository.doRequest(url)
        apiRepository.doRequest(url2)
        verify(apiRepository).doRequest(url)
        verify(apiRepository).doRequest(url2)
    }
}