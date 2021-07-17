package com.elbehiry.shared.usecase

import app.cash.turbine.test
import com.elbehiry.shared.domain.FlowUseCase
import com.elbehiry.shared.result.Result
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.runBlockingTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

class FlowUseCaseTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var useCase: ExceptionUseCase

    @Before
    fun setup() {
        testDispatcher = coroutineRule.testDispatcher
        useCase = ExceptionUseCase(testDispatcher)
    }

    @ExperimentalTime
    @Test
    fun `exception emits Result#Error`() {
        coroutineRule.runBlockingTest {
            useCase(Unit).test {
                assert(expectItem() is Result.Error)
                expectComplete()
            }
        }
    }

    inner class ExceptionUseCase(dispatcher: CoroutineDispatcher) :
        FlowUseCase<Unit, Unit>(dispatcher) {
        override fun execute(parameters: Unit): Flow<Result<Unit>> = flow {
            throw Exception("Test exception")
        }
    }
}