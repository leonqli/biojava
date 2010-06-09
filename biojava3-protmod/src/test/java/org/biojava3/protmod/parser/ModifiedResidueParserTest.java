/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on Jun 7, 2010
 * Author: Jianjiong Gao 
 *
 */

package org.biojava3.protmod.parser;

import java.io.IOException;

import java.net.URL;

import java.util.List;

import junit.framework.TestCase;

import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;

import org.biojava3.protmod.ModificationCategory;
import org.biojava3.protmod.ModifiedCompound;
import org.biojava3.protmod.ProteinModification;

/**
 * 
 * @author Jianjiong Gao
 * @since 3.0
 */
public class ModifiedResidueParserTest extends TestCase {

	public void testParser() throws IOException {
		System.out.println("===Begin testing on ModifiedResidueParser");
		
		//URL url = ModifiedResidueParserTest.class.getResource("3MVJ.pdb");
		String server = "http://www.pdb.org/pdb/files/";
		String name;
		URL url;
		
		name = "3MVJ"; // SEP (phosphoserine) and TPO (phosphothreonine)
		System.out.println(name);
		url = new URL(server+name+".pdb");
		assertNotNull(url);
		parserTest(url);		
		
		name = "1KZU"; // FME
		System.out.println(name);
		url = new URL(server+name+".pdb");
		assertNotNull(url);
		parserTest(url);
		
		System.out.println("===End testing on ModifiedResidueParser");
	}
	
	private void parserTest(URL pdbUrl) throws IOException {
		
		PDBFileReader pdbReader = new PDBFileReader();
		Structure struc = pdbReader.getStructure(pdbUrl);
		
		ModifiedResidueParser parser = new ModifiedResidueParser();
		
		int nrmodel = struc.nrModels();
		for (int modelnr=0; modelnr<nrmodel; modelnr++) {
			List<ModifiedCompound> residues = parser.parse(struc, 
					ProteinModification.getByCategory(ModificationCategory.CHEMICAL_MODIFICATION),
					modelnr);
			
			int i=0;
			for (ModifiedCompound residue : residues) {
				Group g = residue.getProteinResidues().get(0);
				Chain chain = g.getParent();
				System.out.println("Modified Residue #"+(++i));
				System.out.println("\t"+g.getPDBName()+"\t"+chain.getName()+"\t"+g.getPDBCode());
			}
		}
	}
}