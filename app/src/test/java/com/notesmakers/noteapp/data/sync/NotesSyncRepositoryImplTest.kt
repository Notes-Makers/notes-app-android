package com.notesmakers.noteapp.data.sync

import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.api.BasePage
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.data.notes.local.NoteDrawableType
import com.notesmakers.noteapp.data.notes.local.PageOutput
import com.notesmakers.noteapp.domain.auth.GetOwnerUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

class NotesSyncRepositoryImplTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private var testDispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private var testScope: TestScope = TestScope(testDispatcher)

    private lateinit var notesSyncRepository: NotesSyncRepositoryImpl

    private var remoteDataSource: NotesRemoteDataSource = mockk(relaxed = true)
    private var localDataSource: LocalDataSource = mockk(relaxed = true)
    private var getOwnerUseCase: GetOwnerUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        notesSyncRepository = NotesSyncRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            getOwnerUseCase = getOwnerUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given older remote note and newer local note, should update remote note`() =
        runTest {

            val timeLocalNote = ZonedDateTime.now()
            val timeRemoteNote = "2023-12-03T10:15:30+01:00"

            coEvery { localDataSource.fetchDatabaseNotes() } returns flowOf(
                listOf(
                    stubNote(
                        timeLocalNote
                    )
                )
            ).stateIn(
                testScope
            )
            coEvery { remoteDataSource.fetchApiNotes() } returns listOf(
                stubBaseNotesInfo(
                    timeRemoteNote
                )
            )

            coEvery { remoteDataSource.getNote("10") } returns stubBaseNote(timeRemoteNote)

            notesSyncRepository.syncNotes()

            coVerify(exactly = 1) { remoteDataSource.updateNote(any(), any(), any()) }
        }

    @Test
    fun `given older local note and newer remote note, should update local note`() =
        runTest {

            val timeLocalNote = ZonedDateTime.now().minusYears(2)
            val timeRemoteNote = "2023-12-03T10:15:30+01:00"

            coEvery { localDataSource.fetchDatabaseNotes() } returns flowOf(
                listOf(
                    stubNote(
                        timeLocalNote
                    )
                )
            ).stateIn(
                testScope
            )
            coEvery { remoteDataSource.fetchApiNotes() } returns listOf(
                stubBaseNotesInfo(
                    timeRemoteNote
                )
            )

            coEvery { remoteDataSource.getNote("10") } returns stubBaseNote(timeRemoteNote)

            notesSyncRepository.syncNotes()

            coVerify(exactly = 1) { localDataSource.updateNote(any(), any(), any(), any()) }
        }
}

//LocalDatabase stubs
fun stubNote(time: ZonedDateTime) = Note(
    id = "10",
    remoteId = "10",
    name = "Example Note",
    description = "This is an example note",
    noteType = "Personal",
    pages = listOf(stubPageOutput()),
    createdAt = time,
    createdBy = "UserA",
    modifiedBy = "UserA",
    modifiedAt = time,
    isPrivate = true,
    isShared = false,
    isPinned = false,
    tag = listOf("example", "test")
)

fun stubPageOutput() = PageOutput(
    id = "10",
    createdAt = ZonedDateTime.now(),
    createdBy = "UserA",
    modifiedAt = ZonedDateTime.now(),
    modifiedBy = "UserA",
    bitmapDrawables = emptyList(),
    pathDrawables = emptyList(),
    textDrawables = emptyList(),
)

//RemoteDatabase stubs
fun stubBaseNotesInfo(time: String) = BaseNotesInfo(
    noteId = "10",
    name = "Sample Note",
    type = NoteDrawableType.PAINT_NOTE,
    description = "This is a sample note description.",
    createdAt = time,
    createdBy = "UserA",
    modifiedAt = time,
    modifiedBy = "UserB",
    isPrivate = true,
    isShared = false,
    isDeleted = false,
    pageSize = null,
    itemSize = null
)

fun stubBaseNote(time: String) = BaseNote(
    id = "10",
    name = "Sample Note",
    type = NoteDrawableType.PAINT_NOTE,
    description = "This is a sample note description.",
    createdAt = time,
    createdBy = "UserA",
    modifiedAt = time,
    modifiedBy = "UserB",
    isPrivate = true,
    isShared = false,
    isDeleted = false,
    tag = emptyList(),
    pages = listOf(exampleBasePage)
)

val exampleBasePage = BasePage(
    id = "10",
    isDeleted = false,
    createdAt = "2007-12-03T10:15:30+01:00",
    createdBy = "UserA",
    modifiedAt = "2007-12-03T10:15:30+01:00",
    modifiedBy = "UserB",
    items = emptyList()
)
