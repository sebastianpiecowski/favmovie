package favmovie.recommender.exception

import com.recombee.api_client.exceptions.ResponseException

class RecombeeException(ex: ResponseException): Exception(ex)