/*
 * SPDX-FileCopyrightText: (C) Copyright 2023 Regione Piemonte
 *
 * SPDX-License-Identifier: EUPL-1.2 */

package it.simonecelia.tgdownloaderbot;

import org.jsoup.Jsoup;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TGDownloaderBot {

	public static void main ( String[] args ) {
		String instagramUrl = "https://www.instagram.com/reel/DA5lu37NDjj/";

		try {
			// Step 1: Ottieni il sorgente della pagina
			var doc = Jsoup.connect ( instagramUrl )
							.userAgent ( "Mozilla/5.0" )
							.get ();

			// Step 2: Cerca il tag meta con il link al video
			var videoMeta = doc.selectFirst ( "meta[property=og:video]" );
			if ( videoMeta == null ) {
				System.out.println ( "Nessun video trovato su questa pagina." );
				return;
			}

			String videoUrl = videoMeta.attr ( "content" );
			System.out.println ( "Video trovato: " + videoUrl );

			// Step 3: Scarica il video
			downloadVideo ( videoUrl, "video.mp4" );
			System.out.println ( "Video scaricato come 'video.mp4'." );

		} catch ( Exception e ) {
			e.printStackTrace ();
		}
	}

	private static void downloadVideo ( String videoUrl, String outputFileName ) throws Exception {
		URL url = new URL ( videoUrl );
		HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
		connection.setRequestProperty ( "User-Agent", "Mozilla/5.0" );
		connection.connect ();

		try ( InputStream in = connection.getInputStream ();
						FileOutputStream out = new FileOutputStream ( outputFileName ) ) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ( ( bytesRead = in.read ( buffer ) ) != -1 ) {
				out.write ( buffer, 0, bytesRead );
			}
		}
	}
}
