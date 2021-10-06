package ca.tetervak.donutdata3.ui.wikipedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.WikiFragmentBinding

class WikiFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = WikiFragmentBinding.inflate(layoutInflater)

        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl(getString(R.string.wiki_donut_ulr))

        return binding.root
    }

}