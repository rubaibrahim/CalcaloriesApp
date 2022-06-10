package com.gp2.calcalories.remote.config

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gp2.calcalories.common.base.App
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.ProgressDialog
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.remote.model.general.request.GeneralRequest
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.meal.request.GetMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.PostMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.PostMealTypeRequest
import com.gp2.calcalories.remote.model.meal.response.GetMealRecipesResponse
import com.gp2.calcalories.remote.model.meal.response.GetMealTypesResponse
import com.gp2.calcalories.remote.model.meal.response.PostMealTypeResponse
import com.gp2.calcalories.remote.model.plan.request.PostUserPlanMealRequest
import com.gp2.calcalories.remote.model.plan.response.GetUserPlanMealResponse
import com.gp2.calcalories.remote.model.plan.response.GetUserPlansResponse
import com.gp2.calcalories.remote.model.user.request.AuthRequest
import com.gp2.calcalories.remote.model.user.request.GetNotificationRequest
import com.gp2.calcalories.remote.model.user.request.PostUserDetailsRequest
import com.gp2.calcalories.remote.model.user.request.PostUserRequest
import com.gp2.calcalories.remote.model.user.response.GetUserNotificationsResponse
import com.gp2.calcalories.remote.model.user.response.GetUserResponse
import kotlinx.coroutines.*
import retrofit2.Response

class AppRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private var progress: ProgressDialog? = null


    companion object {
        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppRepository? = null
        fun getInstance(): AppRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppRepository().also { INSTANCE = it }
            }
        }
    }


    // Utils  ======================================================================================

    private fun isInternetAvailable(): Boolean {
        if (Utility.isInternetAvailable()) {
            App.context?.let {
                if (progress == null)
                    progress = ProgressDialog(it)
                progress?.show()
            }
            return true
        }
        return false
    }

    private suspend fun <T> setResponse(data: Response<T>, response: (T) -> Unit) {
        withContext(Dispatchers.Main) {
            progress?.dismiss()
            if (data.isSuccessful && data.body() is GeneralResponse) {
                (data.body() as GeneralResponse).let {
                    when (it.status) {
                        HTTPCode.Success, HTTPCode.ValidatorError -> {
                            data.body()?.let { response(it) }
                        }
                        HTTPCode.Forbidden, HTTPCode.Unauthorized -> {
                            App.context?.let { it1 -> Utility.askToLoginInAgain(it1) }
                        }
                        HTTPCode.NoContent, HTTPCode.BadRequest, HTTPCode.NotFound, HTTPCode.Exception -> {
                            App.context?.let { it1 -> Alert.toast(it1, it.message) }
                        }
                        else -> {
                            App.context?.let { it1 -> Alert.toast(it1, it.message) }
                        }
                    }
                }
            } else {
                App.context?.let { it1 -> Alert.toast(it1, data.message().toString()) }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        progress?.dismiss()
        Alert.log("onError", "Exception handled: ${throwable.localizedMessage}")
        throwable.printStackTrace()
    }


    // Auth By Email
    fun authLogin(request: AuthRequest, response: (GetUserResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.authLogin(request), response)
        }
    }

    fun authRegister(request: PostUserRequest, response: (GetUserResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.authRegister(request), response)
        }
    }

     fun authLogout(response: (GeneralResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.authLogout(), response)
        }
    }

    // User
     fun getUser(id: Int,response: (GeneralResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getUser(id), response)
        }
    }
     fun updateUser(id: Int, user: PostUserRequest,response: (GetUserResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.updateUser(id,user), response)
        }
    }

     fun updateUserDetails(request: PostUserDetailsRequest,response: (GetUserResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.updateUserDetails(request), response)
        }
    }


    // User\Notification
     fun getUserNotification(request: GetNotificationRequest,response: (GetUserNotificationsResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getUserNotification(request.toHashMap()), response)
        }
    }
     fun updateUserNotification(id: Int,response: (GeneralResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.updateUserNotification(id), response)
        }
    }


    // Meal  =======================================================================================
    // Meal\Types
     fun getMealTypes(request: GeneralRequest,response: (GetMealTypesResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getMealTypes(request.toHashMap()), response)
        }
    }
     fun postMealType(request: PostMealTypeRequest,response: (PostMealTypeResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.postMealType(request), response)
        }
    }


    // Meal\Recipes
     fun getMealRecipes(request: GetMealRecipeRequest,response: (GetMealRecipesResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getMealRecipes(request.toHashMap()), response)
        }
    }
     fun getMealRecipes2(request: PostMealRecipeRequest,response: (GetMealRecipesResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getMealRecipes2(request), response)
        }
    }


    // Plan  ===================================================================================
    // Plan\User Plans
     fun getUserPlans(request: GetNotificationRequest,response: (GetUserPlansResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.getUserPlans(request.toHashMap()), response)
        }
    }

    // Plan\Plan Meals
     fun postUserPlanMeal(request: PostUserPlanMealRequest,response: (GetUserPlanMealResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.postUserPlanMeal(request), response)
        }
    }
     fun deleteUserPlanMeal(id: Int,response: (GeneralResponse) -> Unit) {
        if (isInternetAvailable()) viewModelScope.launch(ioDispatcher + exceptionHandler) {
            setResponse(Retrofit.API.deleteUserPlanMeal(id), response)
        }
    }
}