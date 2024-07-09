package com.colledk.community.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.colledk.user.domain.pagination.UserPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val userPagingSource: UserPagingSource
) : ViewModel() {
    val users = Pager(
        PagingConfig(pageSize = UserPagingSource.PAGE_SIZE)
    ) {
        userPagingSource
    }.flow.cachedIn(viewModelScope)
}