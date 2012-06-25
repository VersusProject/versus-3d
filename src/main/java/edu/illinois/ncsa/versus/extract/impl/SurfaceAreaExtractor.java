package edu.illinois.ncsa.versus.extract.impl;

import java.util.*;

import edu.illinois.ncsa.versus.*;
import edu.illinois.ncsa.versus.adapter.*;
import edu.illinois.ncsa.versus.adapter.impl.MeshAdapter;
import edu.illinois.ncsa.versus.descriptor.*;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.ncsa.model.MeshAuxiliary.Face;
import edu.ncsa.model.MeshAuxiliary.Point;

/**
 * Extract the surface area an object that has a mesh.
 *
 * @author Kenton McHenry
 */
public class SurfaceAreaExtractor implements Extractor, HasCategory {

    /**
     * Extract a double array representation of an object that has a mesh.
     *
     * @param adapter an object that has a mesh
     * @return a double array representation of the given object
     */
    private DoubleArrayFeature extract(HasMesh adapter) {
        Vector<Face> faces = adapter.getMesh().getFaces();
        Vector<Point> vertices = adapter.getMesh().getVertices();
        double area = 0;
        double tmpd;

        for (int i = 0; i < faces.size(); i++) {
            tmpd = faces.get(i).getArea(vertices);

            if (!Double.isInfinite(tmpd) && !Double.isNaN(tmpd)) {
                area += tmpd;
            }
        }

        return new DoubleArrayFeature(new double[]{area});
    }

    @Override
    public MeshAdapter newAdapter() {
        return null;
    }

    @Override
    public String getName() {
        return "Surface Area";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws UnsupportedTypeException {
        if (adapter instanceof HasMesh) {
            return extract((HasMesh) adapter);
        } else {
            throw new UnsupportedTypeException();
        }
    }

    @Override
    public Set<Class<? extends Adapter>> supportedAdapters() {
        Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();

        adapters.add(HasFaces.class);

        return adapters;
    }

    @Override
    public Class<? extends Descriptor> getFeatureType() {
        return DoubleArrayFeature.class;
    }

    @Override
    public boolean hasPreview() {
        return false;
    }

    @Override
    public String previewName() {
        return null;
    }

    @Override
    public String getCategory() {
        return "3D";
    }
}