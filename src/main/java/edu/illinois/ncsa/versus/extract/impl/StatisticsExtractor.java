package edu.illinois.ncsa.versus.extract.impl;
import edu.illinois.ncsa.versus.*;
import edu.illinois.ncsa.versus.adapter.*;
import edu.illinois.ncsa.versus.adapter.impl.MeshAdapter;
import edu.illinois.ncsa.versus.descriptor.*;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;

import java.util.*;

/**
 * Extract statistic based features from an object that has vertices.
 * @author Kenton McHenry
 */
public class StatisticsExtractor implements Extractor
{
	/**
	 * Extract a double array representation of an object that has vertices.
	 * @param adapter an object that has vertices
	 * @return a double array representation of the given object
	 */
	private DoubleArrayFeature extract(HasVertices adapter)
	{		
		Vector<double[]> vertices = adapter.getVertices();
		double mean_x = 0;
		double mean_y = 0;
		double mean_z = 0;
		double std_x = 0;
		double std_y = 0;
		double std_z = 0;
		double tmpd;
		
		//Compute mean
		for(int i=0; i<vertices.size(); i++){
			mean_x += vertices.get(i)[0];
			mean_y += vertices.get(i)[1];
			mean_z += vertices.get(i)[2];
		}
		
		mean_x /= vertices.size();
		mean_y /= vertices.size();
		mean_z /= vertices.size();
		
		//Compute standard deviation
		for(int i=0; i<vertices.size(); i++){
			tmpd = vertices.get(i)[0] - mean_x;
			std_x += tmpd*tmpd;
			
			tmpd = vertices.get(i)[1] - mean_y;
			std_y += tmpd*tmpd;
			
			tmpd = vertices.get(i)[2] - mean_z;
			std_z += tmpd*tmpd;
		}
		
		std_x /= vertices.size();
		std_y /= vertices.size();
		std_z /= vertices.size();
		
		return new DoubleArrayFeature(new double[]{mean_x, mean_y, mean_z, std_x, std_y, std_z});
	}
	
	@Override
	public MeshAdapter newAdapter()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "Statistics";
	}

	@Override
	public Descriptor extract(Adapter adapter) throws UnsupportedTypeException
	{
		if(adapter instanceof HasVertices){
			return extract((HasVertices)adapter);
		}else{
			throw new UnsupportedTypeException();
		}
	}

	@Override
	public Set<Class<? extends Adapter>> supportedAdapters()
	{
		Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();
		
		adapters.add(HasVertices.class);
		
		return adapters;
	}

	@Override
	public Class<? extends Descriptor> getFeatureType()
	{
		return DoubleArrayFeature.class;
	}
	
	@Override
	public boolean hasPreview(){
		return false;
	}
}