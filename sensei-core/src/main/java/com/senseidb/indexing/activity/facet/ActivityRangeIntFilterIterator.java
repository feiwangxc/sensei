package com.senseidb.indexing.activity.facet;

import java.io.IOException;

import org.apache.lucene.search.DocIdSetIterator;

/**
 * Performs range iteration over activity fields
 * @author vzhabiuk
 *
 */
public class ActivityRangeIntFilterIterator extends DocIdSetIterator {
  private int _doc;
  protected final int[] fieldValues;
  private final int start;
  private final int end;
  private final int arrLength;
  private final int[] indexes;

  public ActivityRangeIntFilterIterator(int[] fieldValues, int[] indexes, int start, int end) {
    this.fieldValues = fieldValues;
    this.start = start;
    this.end = end;
    this.indexes = indexes;
    arrLength = indexes.length;
    _doc = -1;
  }

  @Override
  final public int docID() {
    return _doc;
  }

  @Override
  public int nextDoc() throws IOException {
    while (++_doc < arrLength) {
      if (indexes[_doc] == -1) {
        continue;
      }
      int value = fieldValues[indexes[_doc]];
      if (value >= start && value < end && value != Integer.MIN_VALUE) {
        return _doc;
      }
    }
    return NO_MORE_DOCS;
  }

  @Override
  public int advance(int id) throws IOException {
    _doc = id - 1;
    return nextDoc();
  }

  @Override
  public long cost() {
    // TODO Auto-generated method stub
    return 0;
  }
}
