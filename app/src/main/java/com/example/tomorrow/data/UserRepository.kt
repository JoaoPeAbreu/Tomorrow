package com.example.tomorrow.data

class UserRepository(private val dao: UserDao) {
    suspend fun getAll(): List<UserEntity> = dao.getAllUsers()
    suspend fun getUser(userId: Int): UserEntity? = dao.getUserById(userId)
    suspend fun addUser(user: UserEntity) = dao.insertUser(user)
    suspend fun removeUser(user: UserEntity) = dao.deleteUser(user)
    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

}