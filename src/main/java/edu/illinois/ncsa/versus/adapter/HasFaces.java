package edu.illinois.ncsa.versus.adapter;

import java.util.Vector;

import edu.illinois.ncsa.versus.adapter.Adapter;

/**
 * Anything that has faces connecting vertices.
 * 
 * @author Kenton McHenry
 */
public interface HasFaces extends Adapter {
	/**
	 * Get faces.
	 * 
	 * @return a collection of faces referencing vertices.
	 */
	Vector<int[]> getFaces();

	/**
	 * Get one face at a time.
	 * 
	 * @param index
	 *            the index of the face
	 * @return the face at the given index
	 */
	int[] getFace(int index);
}