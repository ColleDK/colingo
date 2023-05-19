package com.colledk.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.country.domain.model.Country
import com.colledk.onboarding.domain.usecase.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    private val _tabs = MutableStateFlow<List<TabItem>>(emptyList())
    val tabs: StateFlow<List<TabItem>> = _tabs

    init {
        setupCountries()
        setupTabs()
    }

    private fun setupTabs() {
        _tabs.value = listOf(
            TabItem(
                R.string.onboarding_create_user_page,
                R.string.onboarding_create_user_name
            )
        )
    }

    private fun setupCountries() {
        viewModelScope.launch {
            getCountriesUseCase()
                .onSuccess {
                    updateUiState(countries = it)
                }
                .onFailure {
                    _error.send(element = it)
                }
        }
    }

    private fun updateUiState(
        countries: List<com.colledk.country.domain.model.Country>? = null
    ) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            countries = countries ?: currentState.countries
        )
    }

}