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

// ViewModel for managing tennis data: rankings, players, tournaments, live events
class TennisViewModel(private val repository: TennisRepository) : ViewModel() {

    // ---------------------------
    // Player Rankings
    // ---------------------------
    private val _rankings = mutableStateOf<List<PlayerRanking>>(emptyList())
    val rankings: State<List<PlayerRanking>> = _rankings

    private val _selectedPlayer = mutableStateOf<PlayerRanking?>(null)
    val selectedPlayer: State<PlayerRanking?> = _selectedPlayer

    // ---------------------------
    // Player Details
    // ---------------------------
    private val _playerDetails = mutableStateOf<PlayerDetails?>(null)
    val playerDetails: State<PlayerDetails?> = _playerDetails

    // ---------------------------
    // Live Events
    // ---------------------------
    private val _liveEvents = mutableStateOf<List<Event>>(emptyList())
    val liveEvents: State<List<Event>> = _liveEvents

    private val _selectedEvent = mutableStateOf<Event?>(null)
    val selectedEvent: State<Event?> = _selectedEvent

    // ---------------------------
    // Tournaments
    // ---------------------------
    private val _selectedTournament = mutableStateOf<TournamentInfo?>(null)
    val selectedTournament: State<TournamentInfo?> = _selectedTournament

    private val _tournamentDetails = mutableStateOf<TournamentDetails?>(null)
    val tournamentDetails: State<TournamentDetails?> = _tournamentDetails

    private val _tournaments = mutableStateOf<List<TournamentInfo>>(emptyList())
    val tournaments: State<List<TournamentInfo>> = _tournaments

    // ---------------------------
    // Load player rankings from repository
    // ---------------------------
    fun loadRankings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getPlayerRankings()
                _rankings.value = data
                if (data.isNotEmpty()) {
                    // Automatically select top-ranked player
                    _selectedPlayer.value = data[0]
                }
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch rankings", e)
            }
        }
    }

    // Update selected player
    fun selectPlayer(player: PlayerRanking) {
        _selectedPlayer.value = player
    }

    // Update selected tournament
    fun selectTournament(tournament: TournamentInfo){
        _selectedTournament.value = tournament
    }

    // Load detailed info for a player
    fun loadPlayerDetails(playerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000) // optional delay (simulate loading or API latency)
                val details = repository.getPlayerDetails(playerId)
                _playerDetails.value = details
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch player details", e)
                _playerDetails.value = null
            }
        }
    }

    // Load tournaments from both endpoints and sort by date
    fun loadTournaments(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tempData = repository.getTournamentInfo1() + repository.getTournamentInfo2()
                val data = tempData.sortedByDescending { it.date }
                _tournaments.value = data
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch tournaments", e)
            }
        }
    }

    // Load detailed info for a specific tournament
    fun loadTournamentDetails(tournamentID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data= repository.getTournamentDetails(tournamentID)
                _tournamentDetails.value = data
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch tournament details", e)
                _tournamentDetails.value = null
            }
        }
    }

    // Load live tennis events from repository
    fun loadLiveEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val events = repository.getRecentEventDetails()
                _liveEvents.value = events
                if (events.isNotEmpty()) {
                    // Automatically select most recent/live event
                    _selectedEvent.value = events[0]
                }
            } catch (e: Exception) {
                Log.e("TennisViewModel", "Failed to fetch live events", e)
                _liveEvents.value = emptyList()
            }
        }
    }

    // Update selected live event
    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }
}
