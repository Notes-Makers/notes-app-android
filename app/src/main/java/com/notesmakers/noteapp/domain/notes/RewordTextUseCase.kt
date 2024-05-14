package com.notesmakers.noteapp.domain.notes

import com.notesmakers.ai.domain.AiDomain
import org.koin.core.annotation.Factory

@Factory
class RewordTextUseCase(private val aiDomain: AiDomain) {
    suspend operator fun invoke(text: String) = aiDomain.rewordText(text)
}