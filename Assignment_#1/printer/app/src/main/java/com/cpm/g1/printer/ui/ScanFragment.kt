package com.cpm.g1.printer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cpm.g1.printer.ConfigHTTP
import com.cpm.g1.printer.MainActivity
import com.cpm.g1.printer.R
import com.cpm.g1.printer.httpService.GetReceipt
import com.google.gson.Gson
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity

class ScanFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanButton = view.findViewById<Button>(R.id.scan)
        scanButton.setOnClickListener {
            val intent = Intent(this.context, CaptureActivity::class.java)
            intent.putExtra(
                "SCAN_FORMATS",
                "QR_CODE"
            )
            intent.action = Intents.Scan.ACTION
            openScan.launch(intent)
        }
    }

    private val openScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    val mainActivity = activity as MainActivity
                    val address = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/basket/receipt"
                    val basketUUID: String? = it.data?.getStringExtra(Intents.Scan.RESULT)
                    val signedContent = Gson().toJson(hashMapOf("basketUUID" to basketUUID))
                    Thread(GetReceipt(mainActivity, address, signedContent)).start()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this.context, getText(R.string.scan_cancelled), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this.context, getText(R.string.scan_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
}