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
 * Normalized cross correlation.
 * @author Kenton McHenry
 */
public class NormalizedCrossCorrelationMeasure implements Measure
{
	/**
	 * Compare two arrays of the same length.
	 * @param feature1 an array
	 * @param feature2 another array
	 * @return the normalized cross correlation
	 * @throws Exception
	 */
	private SimilarityNumber compare(DoubleArrayFeature feature1, DoubleArrayFeature feature2) throws Exception
	{
		double mean1 = 0;
		double mean2 = 0;
		double std1 = 0;
		double std2 = 0;
		double sum = 0;
		double tmpd;
		
		//Check for same length
		if(feature1.getLength() != feature2.getLength()){
			throw new Exception("Features must have the same length");
		}

		//Calculate means
		for(int i=0; i<feature1.getLength(); i++){
			mean1 += feature1.getValue(i);
			mean2 += feature2.getValue(i);
		}
		
		mean1 /= feature1.getLength();
		mean2 /= feature2.getLength();
		
		//Calculate standard deviations
		for(int i=0; i<feature1.getLength(); i++){
			tmpd = feature1.getValue(i)-mean1;
			std1 += tmpd*tmpd;
			
			tmpd = feature2.getValue(i)-mean2;
			std2 += tmpd*tmpd;
		}
		
		std1 = Math.sqrt(std1 / feature1.getLength());
		std2 = Math.sqrt(std2 / feature2.getLength());

		//Calculate difference
		for(int i=0; i<feature1.getLength(); i++){
			sum += ((feature1.getValue(i)-mean1)*(feature2.getValue(i)-mean2))/(std1*std2);
		}
		
		return new SimilarityNumber(sum / (feature1.getLength()-1));
	}
	
	/**
	 * Compare two arrays of pixels of the same size.
	 * @param feature1
	 * @param feature2
	 * @return the normalized cross correlation
	 * @throws Exception
	 */
	private SimilarityNumber compare(ThreeDimensionalDoubleArrayFeature feature1, ThreeDimensionalDoubleArrayFeature feature2) throws Exception 
	{
		double mean1, mean2;
		double std1, std2;
		double sum, result = 0;
		int n = feature1.getWidth() * feature1.getHeight();
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
		
		for(int band = 0; band < feature1.getNumBands(); band++){
			//Calculate means
			mean1 = 0;
			mean2 = 0;
			
			for(int y = 0; y < feature1.getHeight(); y++){
				for(int x = 0; x < feature1.getWidth(); x++){
					mean1 += feature1.getValue(y, x, band);
					mean2 += feature2.getValue(y, x, band);
				}
			}
			
			mean1 /= n;
			mean2 /= n;
			
			//Calculate standard deviations
			std1 = 0;
			std2 = 0;
			
			for(int y = 0; y < feature1.getHeight(); y++){
				for(int x = 0; x < feature1.getWidth(); x++){
					tmpd = feature1.getValue(y, x, band) - mean1;
					std1 += tmpd*tmpd;
					
					tmpd = feature2.getValue(y, x, band) - mean2;
					std2 += tmpd*tmpd;
				}
			}
			
			std1 = Math.sqrt(std1 / n);
			std2 = Math.sqrt(std2 / n);

			//Calculate difference
			sum = 0;
			
			for(int y = 0; y < feature1.getHeight(); y++){
				for(int x = 0; x < feature1.getWidth(); x++){
					sum += ((feature1.getValue(y, x, band)-mean1)*(feature2.getValue(y, x, band)-mean2))/(std1*std2);
				}
			}
			
			result += sum / (n-1);
		}

		return new SimilarityNumber(result / feature1.getNumBands());
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
		return "Normalized Cross Correlation";
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
	public Class<NormalizedCrossCorrelationMeasure> getType()
	{
		return NormalizedCrossCorrelationMeasure.class;
	}
}