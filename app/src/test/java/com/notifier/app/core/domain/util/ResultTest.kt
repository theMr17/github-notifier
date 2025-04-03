package com.notifier.app.core.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultTest {
    @Test
    fun testSuccessResult_containsCorrectData() {
        val result: Result<Int, DomainError> = Result.Success(42)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(42)
    }

    @Test
    fun testErrorResult_containsCorrectError() {
        val error = object : DomainError {}
        val result: Result<Int, DomainError> = Result.Error(error)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(error)
    }

    @Test
    fun testMap_withSuccessResult_transformsData() {
        val result: Result<Int, DomainError> = Result.Success(10)
        val mappedResult = result.map { it * 2 }

        assertThat(mappedResult).isInstanceOf(Result.Success::class.java)
        assertThat((mappedResult as Result.Success).data).isEqualTo(20)
    }

    @Test
    fun testMap_withErrorResult_preservesError() {
        val error = object : DomainError {}
        val result: Result<Int, DomainError> = Result.Error(error)
        val mappedResult = result.map { it * 2 }

        assertThat(mappedResult).isInstanceOf(Result.Error::class.java)
        assertThat((mappedResult as Result.Error).error).isEqualTo(error)
    }

    @Test
    fun testOnSuccess_withSuccessResult_executesAction() {
        var onSuccessExecuted = false
        val result: Result<Int, DomainError> = Result.Success(5)
        result.onSuccess { onSuccessExecuted = true }

        assertThat(onSuccessExecuted).isTrue()
    }

    @Test
    fun testOnSuccess_withErrorResult_doesNotExecuteAction() {
        var onSuccessExecuted = false
        val error = object : DomainError {}
        val result: Result<Int, DomainError> = Result.Error(error)
        result.onSuccess { onSuccessExecuted = true }

        assertThat(onSuccessExecuted).isFalse()
    }

    @Test
    fun testOnError_withErrorResult_executesAction() {
        var handledError: DomainError? = null
        val error = object : DomainError {}
        val result: Result<Int, DomainError> = Result.Error(error)
        result.onError { handledError = it }

        assertThat(handledError).isEqualTo(error)
    }

    @Test
    fun testOnError_withSuccessResult_doesNotExecuteAction() {
        var handledError: DomainError? = null
        val result: Result<Int, DomainError> = Result.Success(42)
        result.onError { handledError = it }

        assertThat(handledError).isNull()
    }

    @Test
    fun testAsEmptyDataResult_withSuccessResult_discardsData() {
        val result: Result<Int, DomainError> = Result.Success(100)
        val emptyResult = result.asEmptyDataResult()

        assertThat(emptyResult).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun testAsEmptyDataResult_withErrorResult_retainsError() {
        val error = object : DomainError {}
        val result: Result<Int, DomainError> = Result.Error(error)
        val emptyResult = result.asEmptyDataResult()

        assertThat(emptyResult).isInstanceOf(Result.Error::class.java)
        assertThat((emptyResult as Result.Error).error).isEqualTo(error)
    }
}
