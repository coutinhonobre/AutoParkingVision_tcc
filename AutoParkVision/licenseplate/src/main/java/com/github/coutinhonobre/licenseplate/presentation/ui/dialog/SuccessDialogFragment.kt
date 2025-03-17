package com.github.coutinhonobre.licenseplate.presentation.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SuccessDialogFragment(
    private val onConfirm: () -> Unit,
    private val onDismiss: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Entrada Realizada com Sucesso")
            .setMessage("Deseja fazer uma nova entrada?")
            .setPositiveButton("Sim") { _, _ -> onConfirm() }
            .setNegativeButton("Não") { _, _ -> onDismiss() }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onDismiss()
    }
}
