package com.example.tomorrow.data

class UserRepository(private val dao: UserDao) {
    suspend fun getAll(): List<UserEntity> = dao.getAll()
    suspend fun getUser(userId: Int): UserEntity? = dao.getById(userId)
    suspend fun addUser(user: UserEntity) = dao.insert(user)
    suspend fun removeUser(user: UserEntity) = dao.delete(user)
}