package cn.ideal.wf.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.ideal.wf.App;
import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.service.NodeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class NodeServiceTest extends TestCase{

	@Autowired
	private NodeService nodeService;
	@Test
	public void testTree() {
		Long moduleId = 1l;
		Node[][] nodes = nodeService.getTreeNodes(moduleId);
		for(int i=0; i<nodes.length; i++){
			for(int j=0; j<nodes[i].length; j++){
				if(nodes[i][j] != null){
					if(nodes[i][j].getStyle().equals("user")){
						System.out.print(" *  ");
					}
					if(nodes[i][j].getStyle().equals("node")){
						System.out.print(nodes[i][j].getNodename()+"("+nodes[i][j].getHeight()+","+nodes[i][j].getDepth()+","+nodes[i][j].getInnerHeight()+")"+" ");
					}
					if(nodes[i][j].getStyle().equals("pointer")){
						System.out.print("--> ");
					}
					//System.out.print("节点名称："+nodes[i][j].getNodename() );
					//System.out.print("节点深度" + nodes[i][j].getDepth() );
					//System.out.println("节点高度" + nodes[i][j].getHeight() );
				}else{
					System.out.print("    ");
				}
			}
			System.out.println();
		}		
	}
}
