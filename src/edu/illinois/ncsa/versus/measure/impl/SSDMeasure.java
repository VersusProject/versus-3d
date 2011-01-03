package edu.illinois.ncsa.versus.measure.impl;
import edu.illinois.ncsa.versus.UnsupportedTypeException;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.descriptor.impl.ThreeDimensionalDoubleArrayFeature;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import edu.illinois.ncsa.versus.measure.SimilarityNumber;
import edu.illinois.ncsa.versus.measure.SimilarityPercentage;

/**
 * Sum of squared differences.
 * @author Kenton McHenry
 */
public class SSDMeasure implements Measure
{
	/**
	 * Compare two arrays of the same length.
	 * @param feature1 an array
	 * @param feature2 another array
	 * @return the sum of squared differences
	 * @throws Exception
	 */
	private SimilarityNumber compare(DoubleArrayFeature feature1, DoubleArrayFeature feature2) throws Exception
	{
		double sum = 0;
		double tmpd;
		
		//Check for same length
		if(feature1.getLength() != feature2.getLength()){
			throw new Exception("Features must have the same length");
		}

		for(int i=0; i<feature1.getLength(); i++){
			tmpd = feature1.getValue(i)-feature2.getValue(i);
			sum += tmpd*tmpd;
		}

		return new SimilarityNumber(sum);
	}
	
	/**
	 * Compare two arrays of pixels of the same size.
	 * @param feature1
	 * @param feature2
	 * @return
	 * @throws Exception
	 */
	private SimilarityNumber compare(ThreeDimensionalDoubleArrayFeature feature1, ThreeDimensionalDoubleArrayFeature feature2) throws Exception 
	{		
		double sum = 0;
		double tmpd;
		
		//Check for same height
		if(feature1.getHeight() != feature2.getHeight()){
			throw new Exception("Features must have the same height");
		}
		
		//Check for same width
		if(feature1.getWidth() != feature2.getWidth()){
			throw new Exception("Features must have the same width");
		}
		
		//Check for same number of bands
		if(feature1.getNumBands() != feature2.getNumBands()){
			throw new Exception("Features must have the same width");
		}
		
		for(int row = 0; row < feature1.getHeight(); row++){
			for(int col = 0; col < feature1.getWidth(); col++){
				for(int band = 0; band < feature1.getNumBands(); band++){
					tmpd = feature1.getValue(row, col, band) - feature2.getValue(row, col, band);
					sum += tmpd*tmpd;
				}
			}
		}

		return new SimilarityNumber(sum);
	}

	@Override
	public SimilarityPercentage normalize(Similarity similarity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFeatureType()
	{
		return ThreeDimensionalDoubleArrayFeature.class.getName();
	}

	@Override
	public String getName()
	{
		return "SSD";
	}

	@Override

	public Similarity compare(Descriptor feature1, Descriptor feature2) throws Exception
	{
		if(feature1 instanceof DoubleArrayFeature && feature2 instanceof DoubleArrayFeature){
			return compare((DoubleArrayFeature)feature1, (DoubleArrayFeature)feature2);
		}else if(feature1 instanceof ThreeDimensionalDoubleArrayFeature && feature2 instanceof ThreeDimensionalDoubleArrayFeature){
			return compare((ThreeDimensionalDoubleArrayFeature)feature1, (ThreeDimensionalDoubleArrayFeature)feature2);
		}else{
			throw new UnsupportedTypeException();
		}
	}

	@Override
	public Class<SSDMeasure> getType()
	{
		return SSDMeasure.class;
	}
}