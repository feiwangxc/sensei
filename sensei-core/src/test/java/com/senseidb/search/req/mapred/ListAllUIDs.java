package com.senseidb.search.req.mapred;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ListAllUIDs implements SenseiMapReduce<Serializable, Serializable> {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init(JSONObject params) {
    // TODO Auto-generated method stub

  }

  @Override
  public Serializable map(IntArray docIds, int docIdCount, long[] uids, FieldAccessor accessor,
      FacetCountAccessor facetCountAccessor) {
    ArrayList<Long> collectedUids = new ArrayList<Long>(docIdCount);
    for (int i = 0; i < docIdCount; i++) {
      collectedUids.add(uids[docIds.get(i)]);
    }
    return collectedUids;
  }

  @Override
  public List<Serializable> combine(List<Serializable> mapResults, CombinerStage combinerStage) {
    // TODO Auto-generated method stub
    return mapResults;
  }

  @Override
  public Serializable reduce(List<Serializable> combineResults) {
    // TODO Auto-generated method stub
    return new ArrayList<Serializable>(combineResults);
  }

  @Override
  public JSONObject render(Serializable reduceResult) {
    // TODO Auto-generated method stub
    return new JSONObject();
  }

}
