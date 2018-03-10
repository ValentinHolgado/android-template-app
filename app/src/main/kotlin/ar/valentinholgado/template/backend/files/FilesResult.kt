package ar.valentinholgado.template.backend.files

import ar.valentinholgado.template.backend.Result
import java.io.File

data class FilesResult(val fileList: List<File>,
                  override val status: Status,
                  override val successMessage: String? = null,
                  override val errorMessage: String? = null) : Result()