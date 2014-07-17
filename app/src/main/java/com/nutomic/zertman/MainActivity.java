package com.nutomic.zertman;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends ListActivity {

	private ListView mListView;

	private CertificateAdapter mCertificateAdapter;

	private CertificateManager mCertificateManager;

	private MovedCertificatesStorage mMovedCertificatesStorage;

	/**
	 * Sets up ListView showing all certificates.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    mListView = getListView();
	    mCertificateAdapter = new CertificateAdapter(this);
	    mCertificateManager = new CertificateManager();
	    mMovedCertificatesStorage = new MovedCertificatesStorage(this);

	    mCertificateAdapter.addAll(mCertificateManager.getCertificates(false));
	    mCertificateAdapter.addAll(mMovedCertificatesStorage.list());
	    mListView.setAdapter(mCertificateAdapter);
    }

	@Override
	protected void onResume() {
		super.onResume();
		final List<Certificate> list = mCertificateManager.getCertificates(false);
		if (!list.isEmpty()) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_move_certs_title)
					.setMessage(R.string.dialog_move_certs_message)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							for (Certificate c : list) {
								mCertificateManager.moveCertificateToSystem(c);
								mMovedCertificatesStorage.insert(c);
							}
						}
					})
					.setNegativeButton(android.R.string.no, null)
					.show();
		}
	}
}
