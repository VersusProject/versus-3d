package edu.illinois.ncsa.versus.adapter.impl;

import edu.illinois.ncsa.versus.adapter.FileLoader;
import edu.illinois.ncsa.versus.adapter.HasFaces;
import edu.illinois.ncsa.versus.adapter.HasMesh;
import edu.illinois.ncsa.versus.adapter.HasVertices;
import edu.ncsa.model.*;
import edu.ncsa.model.MeshAuxiliary.*;
import edu.ncsa.utility.*;
import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;

/**
 * Adapter for NCSA 3DUtilities Mesh class.
 * @author Kenton McHenry
 */
public class MeshAdapter implements HasVertices, HasFaces, HasMesh, FileLoader
{
	private Mesh mesh = new Mesh();
	private static Log log = LogFactory.getLog(MeshAdapter.class);

	public MeshAdapter() {}

	/**
	 * Adapter for NCSA 3DUtilities Mesh.
	 * @param image
	 */
	public MeshAdapter(Mesh mesh)
	{
		this.mesh = mesh;
	}

	@Override
	public String getName()
	{
		return "Mesh";
	}

	@Override
	public Vector<double[]> getVertices()
	{
		Vector<double[]> vertices = new Vector<double[]>();
		Point p;
		
		for(int i=0; i<mesh.getVertices().size(); i++){
			p = mesh.getVertex(i);
			vertices.add(new double[]{p.x, p.y, p.z});
		}
		
		return vertices;
	}

	@Override
	public double[] getVertex(int index)
	{
		Point p = mesh.getVertex(index);
		
		return new double[]{p.x, p.y, p.z};
	}
	
	@Override
	public Vector<int[]> getFaces()
	{
		Vector<int[]> faces = new Vector<int[]>();
		
		for(int i=0; i<mesh.getFaces().size(); i++){
			faces.add(mesh.getFace(i).v);
		}
		
		return faces;
	}
	
	@Override
	public int[] getFace(int index)
	{
		return mesh.getFace(index).v;
	}

	@Override
	public Mesh getMesh()
	{
		return mesh;
	}

	@Override
	public void load(File file) throws IOException
	{
		mesh.load(Utility.unixPath(file.getAbsolutePath()));
	}

	@Override
	public List<String> getSupportedMediaTypes() {
		return new ArrayList<String>(Mesh.formats);
	}
}