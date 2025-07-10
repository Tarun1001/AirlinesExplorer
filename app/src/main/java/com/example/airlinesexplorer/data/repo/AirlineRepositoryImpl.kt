package com.example.airlinesexplorer.data.repo
import com.example.airlinesexplorer.data.local.AirlineDao
import com.example.airlinesexplorer.data.local.toAirline
import com.example.airlinesexplorer.data.local.toEntity
import com.example.airlinesexplorer.data.remote.AirlineApiService
import com.example.airlinesexplorer.data.remote.toAirline
import com.example.airlinesexplorer.domain.Airline
import com.example.airlinesexplorer.domain.AirlineRepository
import com.example.airlinesexplorer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AirlineRepositoryImpl @Inject constructor(
    private val api: AirlineApiService,
    private val dao: AirlineDao
) : AirlineRepository {

    override suspend fun getAirlines(): Flow<Resource<List<Airline>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(hardcodedAirlines))

        /*try {
            // Get cached data first
            val cachedAirlines = dao.getAllAirlines().map { entities ->
                entities.map { it.toAirline() }
            }

            // Emit cached data if available
            cachedAirlines.collect { airlines ->
                if (airlines.isNotEmpty()) {
                    emit(Resource.Success(airlines))
                }
            }

            // Fetch fresh data from API
            val remoteAirlines = api.getAirlines()
            val favoriteIds = dao.getFavoriteAirlines().map { entities ->
                entities.map { it.id }
            }

            favoriteIds.collect { favIds ->
                val airlinesWithFavorites = remoteAirlines.map { dto ->
                    dto.toAirline(isFavorite = favIds.contains(dto.id))
                }

                // Cache the data
                dao.deleteAll()
                dao.insertAirlines(airlinesWithFavorites.map { it.toEntity() })

                emit(Resource.Success(airlinesWithFavorites))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }*/
    }

    override suspend fun getAirlineById(id: String): Flow<Resource<Airline>> = flow {
        emit(Resource.Loading())

        try {
            val cachedAirline = dao.getAirlineById(id)
            if (cachedAirline != null) {
                emit(Resource.Success(cachedAirline.toAirline()))
            } else {
                val remoteAirline = api.getAirlineById(id)
                emit(Resource.Success(remoteAirline.toAirline()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun searchAirlines(query: String): Flow<Resource<List<Airline>>> = flow {
        emit(Resource.Loading())

        try {
            val searchResults = dao.searchAirlines("%$query%")
            searchResults.collect { entities ->
                val airlines = entities.map { it.toAirline() }
                emit(Resource.Success(airlines))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun toggleFavorite(airlineId: String) {
        val airline = dao.getAirlineById(airlineId)
        airline?.let {
            val updatedAirline = it.copy(isFavorite = !it.isFavorite)
            dao.updateAirline(updatedAirline)
        }
    }

    override suspend fun getFavoriteAirlines(): Flow<List<Airline>> {
        return dao.getFavoriteAirlines().map { entities ->
            entities.map { it.toAirline() }
        }
    }
}
  val hardcodedAirlines = listOf(
    Airline(
        id = "1",
        name = "EasyJet",
        country = "United Kingdom",
        headquarters = "London Luton Airport",
        fleetSize = 342,
        website = "https://www.easyjet.com",
        logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/EasyJet_logo.svg/2560px-EasyJet_logo.svg.png",
        isFavorite = false
    ),
    Airline(
        id = "2",
        name = "Jet2",
        country = "United Kingdom",
        headquarters = "Leeds Bradford Airport",
        fleetSize = 119,
        website = "https://www.jet2.com",
        logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Jet2.com_logo.svg/2560px-Jet2.com_logo.svg.png",
        isFavorite = false
    )
)