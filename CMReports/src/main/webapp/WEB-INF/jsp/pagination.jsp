<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ page isELIgnored="false" %>
<script type="text/javascript">
<!--
function _scrollPage(p_startIndex, p_totalCnt,p_frmId){
	var objForm = document.getElementById(p_frmId);
	objForm.pageStartIndex.value=p_startIndex;
	objForm.pageTotalRecCount.value = p_totalCnt;
	//alert(objForm.pageStartIndex.value);
}
//-->
</script>


<!--  Pagination calculation -->
<s:set name="targetId">${param.targetId}</s:set>
<s:set name="frmId">${param.frmId}</s:set>

<s:set name="rand">${param.random==null?"0":param.random}</s:set>
<s:if test="%{#rand !=0}">
	<s:property value="%{#rand}" />
</s:if>

<s:set name="totalCount">${param.totalCount}</s:set>
<s:set name="noOfPages" value="%{(#totalCount/perPageRecCount)}"/>
<%request.setAttribute("intNumPges",((Double)request.getAttribute("noOfPages")).intValue());%>
<s:if test="%{(#totalCount % perPageRecCount) > 0}" >
	<s:set name="noOfPages">${intNumPges+1}</s:set>
</s:if>


<s:set name="currentPage"  value="%{(pageStartIndex / perPageRecCount) + 1}" />
<s:set name="startPage" value="%{((#currentPage / numberOfPagesToDisplay) * numberOfPagesToDisplay) + 1}" />
<s:if test="%{(#currentPage % numberOfPagesToDisplay) == 0}" >
	<s:set name="startPage" value="%{#startPage - numberOfPagesToDisplay}" />
</s:if>
<s:set name="endPage" value="%{(#startPage + numberOfPagesToDisplay) - 1}"/>
<s:if test="%{#endPage > #noOfPages}" >
	<s:set name="endPage" value="%{#noOfPages}" />
</s:if>



<s:if test="%{#totalCount > perPageRecCount}">


<hr color="#006390"/>

<div class="records" style="float:right;">Record(s) 
	<s:property value="pageStartIndex+1"/> -
	<s:if test="%{(pageStartIndex + perPageRecCount) > #totalCount}"> 
		<s:property value="totalCount"/>
	</s:if> 
	<s:else>
		<s:property value="pageStartIndex + perPageRecCount"/>
	</s:else>
		of 
	<s:property value="totalCount"/> 
</div> 
<div align="center" class="pagination">

  <table>
    <tbody>
      <tr>
       <!-- First Page Button START -->
      <s:if test="%{#currentPage > 1}">
      	<td class="btn">
			<sj:a  id="page_first_%{#frmId}_%{#rand}"
				   formIds="%{#frmId}" 
				   cssStyle="display:block;text-decoration:none" 
				   targets="%{#targetId}" 
				   onclick="_scrollPage(0,%{#totalCount},'%{#frmId}');return true;"
				   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
				   title="First Page"
				   >
				 First  
			</sj:a>
		</td> 		
      </s:if>
      <s:else>
      	<td class="disabled">First</td> <!-- First Page  Disabled-->
      </s:else>
      <!-- First Page Button End -->
      
      <!-- Previous Pages Set Button START -->
      <s:if test="%{#startPage > 1}">
      	<s:set name="prevPageSet" value="%{((#startPage - numberOfPagesToDisplay) * perPageRecCount)- perPageRecCount}"/>
      	<td class="btn">
			<sj:a  id="page_fastRewind_%{#frmId}_%{#rand}"
				   formIds="%{#frmId}" 
				   cssStyle="display:block;text-decoration:none" 
				   targets="%{#targetId}" 
				   onclick="_scrollPage(%{#prevPageSet},%{#totalCount},'%{#frmId}');return true;"
				   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
				   title="Previous Page Set"
				   >
				 &#171;&#171;  
			</sj:a>
		</td> 		
      </s:if>
      <s:else>
      	<td class="disabled">&#171;&#171;</td> <!-- To Prev Page Set  Disabled-->
      </s:else>

      <!-- Previous Pages Set Button END -->  
       
      <!-- Previous Page Button START -->
      <s:if test="%{pageStartIndex > 0}">
      	<s:set name="pevPageIndx" value="%{pageStartIndex - perPageRecCount}" />
      	<td class="btn">
			<sj:a 
			   id="page_previous_%{#frmId}_%{#rand}"
			   formIds="%{#frmId}" 
			   cssStyle="display:block;text-decoration:none" 
			   targets="%{#targetId}" 
			   onclick="_scrollPage(%{#pevPageIndx},%{#totalCount},'%{#frmId}');return true;"
			   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
			   title="Previous Page"
			   >
			   &#171;
			</sj:a>
		</td>
      </s:if>
      <s:else>
      	<td class=" disabled">&#171;</td> <!-- Prev Page Disabled -->
      </s:else>
      <!-- Previous Page Button END -->
      
      <!-- Page Numbers START -->
      
      <s:iterator var='pageNo' begin="%{#startPage}" end="%{#endPage}">
			<s:if  test="%{#pageNo == #currentPage}">
				<td class="act "><s:property value="#pageNo" /> </td>
			</s:if>
			<s:else>
			<s:set  name="pageIndexVal" value="%{(#pageNo * perPageRecCount) - perPageRecCount}"/>
				<td class="link">
				<sj:a  
					id="page_%{#frmId}_%{#rand}_%{#pageNo}"
					  formIds="%{#frmId}"
					  cssStyle="display:block;text-decoration:none" 
					  targets="%{#targetId}" 
					  onclick="_scrollPage(%{#pageIndexVal},%{#totalCount},'%{#frmId}');return true;"
					  indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
					  title="%{#pageNo}"
					  >
				<s:text name="%{#pageNo}"/>
				</sj:a>
				</td>
			</s:else>
		</s:iterator>
      <!-- Page Numbers END -->  
       
       <!-- Next Page Button START -->
       <s:if test="%{#totalCount > (pageStartIndex + perPageRecCount) }">
       	<s:set name="nxtPageIndx" value="%{pageStartIndex + perPageRecCount}"/>
       	<td class="btn">
       	<sj:a 
       	   id="page_next_%{#frmId}_%{#rand}"
   		   formIds="%{#frmId}" 
		   cssStyle="display:block;text-decoration:none" 
		   targets="%{#targetId}" 
		   onclick="_scrollPage(%{#nxtPageIndx},%{#totalCount},'%{#frmId}');return true;"
		   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
		   title="Next Page"
		   >
       		&#187;
       	</sj:a>
       	</td>
	   </s:if>
	   <s:else>
	   	<td class="disabled">&#187;</td>
	   </s:else>
	   <!-- Next Page Button END -->	
       
        <!-- Next PageSet Button START -->
       <s:if test="%{#totalCount > (#endPage * perPageRecCount) }">
       	<s:set name="nxtPageSetIndx" value="%{(#endPage * perPageRecCount)}"/>
       	<td class="btn">
	       	<sj:a 
	       		   id="page_fastForward_%{#frmId}_%{#rand}"
      			   formIds="%{#frmId}" 
      			   cssStyle="display:block;text-decoration:none" 
      			   targets="%{#targetId}" 
      			   onclick="_scrollPage(%{#nxtPageSetIndx},%{#totalCount},'%{#frmId}');return true;"
      			   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
      			   title="Next Page Set"
      			   >
      			&#187;&#187;   
      		</sj:a>
        </td>
	   </s:if>
	   <s:else>
	   	<td class="disabled">&#187;&#187;</td>
	   </s:else>
	   <!-- Next PageSet Button END -->
	   
	   <!-- Last Page Button START -->
	    
       <s:if test="%{#noOfPages > #currentPage}">
       	<s:set name="lastPageIndx" value="%{(#noOfPages - 1) * perPageRecCount}"/>
       	<td class="btn">
	       	<sj:a 
	       		   id="page_last_%{#frmId}_%{#rand}"
      			   formIds="%{#frmId}" 
      			   cssStyle="display:block;text-decoration:none" 
      			   targets="%{#targetId}" 
      			   onclick="_scrollPage(%{#lastPageIndx},%{#totalCount},'%{#frmId}');return true;"
      			   indicator="loadingBar_%{#frmId}_%{#targetId}_%{#rand}"
      			   title="Last Page"
      			   >
      			Last  
      		</sj:a>
        </td>
	   </s:if>
	   <s:else>
	   	<td class="disabled">Last</td>
	   </s:else>
	   <!-- Last Page Button END -->
	      
      </tr>
    </tbody>
  </table>
</div>
</s:if>
