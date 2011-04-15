package edu.illinois.ncsa.versus.adapter;
import edu.ncsa.model.*;

/**
 * Anything that has a mesh.
 * @author Kenton McHenry 
 */
public interface HasMesh extends Adapter
{
	/**
	 * Get the mesh.
	 * @return a mesh
	 */
	Mesh getMesh();
}