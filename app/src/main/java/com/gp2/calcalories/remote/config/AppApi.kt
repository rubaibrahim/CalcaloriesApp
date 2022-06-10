package com.gp2.calcalories.remote.config

import com.gp2.calcalories.remote.model.meal.response.GetMealRecipesResponse
import com.gp2.calcalories.remote.model.meal.response.GetMealTypesResponse
import retrofit2.Response
import retrofit2.http.*
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.meal.request.GetMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.PostMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.PostMealTypeRequest
import com.gp2.calcalories.remote.model.meal.response.PostMealTypeResponse
import com.gp2.calcalories.remote.model.plan.request.PostUserPlanMealRequest
import com.gp2.calcalories.remote.model.plan.response.GetUserPlanMealResponse
import com.gp2.calcalories.remote.model.plan.response.GetUserPlansResponse
import com.gp2.calcalories.remote.model.user.request.AuthRequest
import com.gp2.calcalories.remote.model.user.request.PostUserDetailsRequest
import com.gp2.calcalories.remote.model.user.request.PostUserRequest
import com.gp2.calcalories.remote.model.user.response.*

interface AppApi {

    // User  =======================================================================================
    // Auth
    @POST("login")
    suspend fun authLogin(@Body request: AuthRequest): Response<GetUserResponse>

    @POST("register")
    suspend fun authRegister(@Body request: PostUserRequest): Response<GetUserResponse>

    @POST("logout")
    suspend fun authLogout(): Response<GeneralResponse>


    // User
    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<GetUserResponse>

    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: PostUserRequest): Response<GetUserResponse>

    @PUT("userDetails")
    suspend fun updateUserDetails(@Body request: PostUserDetailsRequest): Response<GetUserResponse>

    @GET("userNotification")
    suspend fun getUserNotification(@QueryMap options: Map<String, String>): Response<GetUserNotificationsResponse>

    @PUT("userNotification/{id}")
    suspend fun updateUserNotification(@Path("id") id: Int): Response<GeneralResponse>



    // Meal  =======================================================================================
    // Meal\Types
    @GET("mealType")
    suspend fun getMealTypes(@QueryMap options: Map<String, String>): Response<GetMealTypesResponse>

    @POST("mealType")
    suspend fun postMealType(@Body request: PostMealTypeRequest): Response<PostMealTypeResponse>

    // Meal\Recipes
    @GET("mealRecipe")
    suspend fun getMealRecipes(@QueryMap options: Map<String, String>): Response<GetMealRecipesResponse>

    @POST("mealRecipe")
    suspend fun getMealRecipes2(@Body request: PostMealRecipeRequest): Response<GetMealRecipesResponse>




    // Plan  ===================================================================================
    // Plan\User Plans
    @GET("userPlan")
    suspend fun getUserPlans(@QueryMap options: Map<String, String>): Response<GetUserPlansResponse>


    // Plan\Plan Meals
    @POST("userPlanMeal")
    suspend fun postUserPlanMeal(@Body request: PostUserPlanMealRequest): Response<GetUserPlanMealResponse>

    @DELETE("userPlanMeal/{id}")
    suspend fun deleteUserPlanMeal(@Path("id") id: Int): Response<GeneralResponse>
}