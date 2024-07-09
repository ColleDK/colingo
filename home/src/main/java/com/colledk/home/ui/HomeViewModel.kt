package com.colledk.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.colledk.home.domain.pagination.HomePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homePagingSource: HomePagingSource
) : ViewModel() {
    val posts = Pager(
        PagingConfig(pageSize = HomePagingSource.PAGE_SIZE)
    ) {
        homePagingSource
    }.flow.cachedIn(viewModelScope)
}