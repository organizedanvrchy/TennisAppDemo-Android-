package com.example.tennisapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.tennisapp.data.TennisRepository
import com.example.tennisapp.model.eventdetails.Event
import com.example.tennisapp.model.playerdetails.PlayerDetails
import com.example.tennisapp.model.playerrankings.PlayerRanking
import com.example.tennisapp.model.tournamentdetails.TournamentDetails
import com.example.tennisapp.model.tournamentdetails.TournamentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TennisViewModel(private val repository: TennisRepository) : ViewModel() {
    // Player Rankings
    private val _rankings = mutableStateOf<List<PlayerRanking>>(emptyList())
    val rankings: State<List<PlayerRanking>> = _rankings

    private val _selectedPlayer = mutableStateOf<PlayerRanking?>(null)
    val selectedPlayer: State<PlayerRanking?> = _selectedPlayer

    // Player Details
    private val _playerDetails = mutableStateOf<PlayerDetails?>(null)
    val playerDetails: State<PlayerDetails?> = _playerDetails

    // Live Events
    private val _liveEvents = mutableStateOf<List<Event>>(emptyList())
    val liveEvents: State<List<Event>> = _liveEvents

    private val _selectedEvent = mutableStateOf<Event?>(null)
    val selectedEvent: State<Event?> = _selectedEvent

    private val _selectedTournament = mutableStateOf<TournamentInfo?>(null)
    val selectedTournament: State<TournamentInfo?> = _selectedTournament

    private val _tournamentDetails = mutableStateOf<TournamentDetails?>(null)
    val tournamentDetails: State<TournamentDetails?> = _tournamentDetails

    private val _tournaments = mutableStateOf<List<TournamentInfo>>(emptyList())
    val tournaments: State<List<TournamentInfo>> = _tournaments

    fun loadRankings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getPlayerRankings()
                _rankings.value = data
                if (data.isNotEmpty()) {
                    _selectedPlayer.value = data[0] // top-ranked player
                }
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch rankings", e)
            }
        }
    }

    fun selectPlayer(player: PlayerRanking) {
        _selectedPlayer.value = player
    }

    fun selectTournament(tournament: TournamentInfo){
        _selectedTournament.value = tournament
    }

    fun loadPlayerDetails(playerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                val details = repository.getPlayerDetails(playerId)
                _playerDetails.value = details
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch player details", e)
                _playerDetails.value = null
            }
        }
    }

    fun loadTournaments(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tempData = repository.getTournamentInfo1() + repository.getTournamentInfo2()
                val data = tempData.sortedByDescending { it.date }
                _tournaments.value = data

            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch rankings", e)
            }
        }
    }

    fun loadTournamentDetails(tournamentID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data= repository.getTournamentDetails(tournamentID)
                _tournamentDetails.value = data

            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch rankings", e)
                _tournamentDetails.value = null
            }
        }
    }


    fun loadLiveEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val events = repository.getRecentEventDetails()
                _liveEvents.value = events
                if (events.isNotEmpty()) {
                    _selectedEvent.value = events[0] // most recent/live event
                }
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch live events", e)
                _liveEvents.value = emptyList()
            }
        }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }
}