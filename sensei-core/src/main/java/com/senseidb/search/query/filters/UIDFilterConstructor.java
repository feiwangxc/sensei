package com.senseidb.search.query.filters;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.json.JSONObject;

import com.browseengine.bobo.api.BoboSegmentReader;
import com.browseengine.bobo.api.BrowseSelection;
import com.browseengine.bobo.facets.FacetHandler;
import com.senseidb.conf.SenseiFacetHandlerBuilder;
import com.senseidb.search.facet.UIDFacetHandler;
import com.senseidb.util.RequestConverter;

public class UIDFilterConstructor extends FilterConstructor {
  public static final String FILTER_TYPE = "ids";

  @Override
  protected Filter doConstructFilter(Object obj) throws Exception {
    final JSONObject json = (JSONObject) obj;
    return new Filter() {

      @Override
      public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) throws IOException {
        if (context.reader() instanceof BoboSegmentReader) {
          BoboSegmentReader boboReader = (BoboSegmentReader) context.reader();
          FacetHandler<?> uidHandler = boboReader
              .getFacetHandler(SenseiFacetHandlerBuilder.UID_FACET_NAME);
          if (uidHandler != null && uidHandler instanceof UIDFacetHandler) {
            UIDFacetHandler uidFacet = (UIDFacetHandler) uidHandler;
            try {
              String[] vals = RequestConverter.getStrings(json.optJSONArray(VALUES_PARAM));
              String[] nots = RequestConverter.getStrings(json.optJSONArray(EXCLUDES_PARAM));
              BrowseSelection uidSel = new BrowseSelection(SenseiFacetHandlerBuilder.UID_FACET_NAME);
              if (vals != null) {
                uidSel.setValues(vals);
              }
              if (nots != null) {
                uidSel.setNotValues(nots);
              }
              return uidFacet.buildFilter(uidSel).getDocIdSet(context, acceptDocs);
            } catch (Exception e) {
              throw new IOException(e);
            }
          } else {
            throw new IllegalStateException("invalid uid handler " + uidHandler);
          }
        } else {
          throw new IllegalStateException("read not instance of " + BoboSegmentReader.class);
        }
      }
    };
  }

}
