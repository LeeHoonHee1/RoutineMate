package com.example.routinemate.presentation.profile

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

// 갤러리 이미지 Uri를 Multipart 파일로 변환
fun uriToProfileImagePart(
    context: Context,
    uri: Uri
): MultipartBody.Part {

    val contentResolver =
        context.contentResolver

    // 이미지 MIME 타입 확인
    val mimeType =
        contentResolver.getType(uri)
            ?: "image/jpeg"

    // 확장자 확인
    val extension =
        MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(
                mimeType
            )
            ?: "jpg"

    // 앱 캐시에 임시 이미지 파일 생성
    val tempFile =
        File.createTempFile(
            "profile_",
            ".$extension",
            context.cacheDir
        )

    // 선택한 사진을 임시 파일로 복사
    contentResolver
        .openInputStream(uri)
        ?.use { input ->

            tempFile
                .outputStream()
                .use { output ->

                    input.copyTo(output)
                }
        }
        ?: throw IllegalArgumentException(
            "이미지 파일을 읽을 수 없습니다."
        )

    val requestBody =
        tempFile.asRequestBody(
            mimeType.toMediaType()
        )

    // 서버의 @RequestParam("file")과 이름을 맞춤
    return MultipartBody.Part.createFormData(
        name = "file",
        filename = tempFile.name,
        body = requestBody
    )
}