package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class ProvinceApiResponse(
    val success: Boolean,
    val data: List<ProvinceResponse>
)
//@JsonIgnoreProperties(ignoreUnknown = true)
data class ProvinceResponse(
    val kode: String,
    val name: String,
    @SerializedName("is_active")
    val isActive: Int,
    @SerializedName("with_no_rangka")
    val withNoRangka: Int,
    @SerializedName("with_nik")
    val withNik: Int
)

// Helper function untuk get plate codes dari hardcoded mapping (fallback)
private fun getPlateCodesFromMapping(kode: String): List<String> {
    return when (kode) {
        "DKI" -> listOf("B")
        "JBR" -> listOf("D", "E", "F", "T", "Z")
        "JTG" -> listOf("G", "H", "K", "R", "AA", "AD")
        "JTM" -> listOf("L", "M", "N", "P", "S", "W", "AE", "AG")
        "DIY" -> listOf("AB")
        "BNTN" -> listOf("A")
        "BALI" -> listOf("DK")
        "NTB" -> listOf("DR", "EA")
        "NTT" -> listOf("DH", "EB", "ED")
        "ACEH" -> listOf("BL")
        "SUMUT" -> listOf("BK")
        "SUMBAR" -> listOf("BA")
        "RIAU" -> listOf("BM")
        "JMBI" -> listOf("BH")
        "BNKLU" -> listOf("BD")
        "SUMSEL" -> listOf("BG")
        "BDRLMP" -> listOf("BE")
        "BABEL" -> listOf("BN")
        "KEPRI" -> listOf("BP")
        "KALBAR" -> listOf("KB")
        "KALSEL" -> listOf("DA")
        "KALTENG" -> listOf("KH")
        "KALTIM" -> listOf("KT")
        "KALTARA" -> listOf("KU")
        "SULSEL" -> listOf("DD")
        "SULBAR" -> listOf("DC")
        "SULTRA" -> listOf("DT")
        "SULTENG" -> listOf("DN")
        "SULUT" -> listOf("DB")
        "GRNTL" -> listOf("DM")
        "MLK" -> listOf("DE")
        "MALUT" -> listOf("DG")
        "PAPUA" -> listOf("PA")
        "PABAR" -> listOf("PB")
        "PABADA" -> listOf("PB")
        "PATEN" -> listOf("PA")
        "PASEL" -> listOf("PA")
        "PAPEG" -> listOf("PA")
        else -> emptyList()
    }
}

// Extension function untuk convert ke model lokal
fun ProvinceResponse.toProvince() = com.paondev.infoplat.data.Province(
    kode = kode,
    name = name,
    isActive = isActive == 1,
    withNoRangka = withNoRangka == 1,
    withNik = withNik == 1,
    plateCodes = getPlateCodesFromMapping(kode)
)
