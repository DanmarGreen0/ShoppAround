import Table from 'react-bootstrap/Table';

export default function CreateTable({tableData}){

    function CreateTableHeader({contents}){ 

        let tHeader = [];

        for(let i = 0; i < contents.length; i++){
            tHeader[i] = (<th key={i}>{contents[i]}</th>);
        }
    
        return(
            <tr>
                {tHeader}
            </tr>
        );
    };


    function CreateBodyWithMsg({str}){
      
        return (
          <tr id="error-msg-row"><td>{str}</td></tr>
        );
    }

    function CreateTableBody({ contents }) {
    
        let tableRows = [];
        
        
        for (let i = 0; i < contents.length; i++) {
        
            let colCount = 1;
            
            tableRows[i] = (
            <tr key={i}id={"row" + (i + 1)}>
                {Object.entries(contents[i]).map(([k, v]) =>
                    ( ((typeof v !== "object") && v.length !== 0) )
                    ?
                        <td key={colCount} id={"col" + colCount++}>{v}</td>
                    : 
                        <td key={colCount} id={"col" + colCount++}>{"none"}</td>
                )}
            </tr>
            );
        
            colCount = 0;
        
        }
        
        return (tableRows);
    }

    function CreateTable({tableParts}){
       
        return (
        <Table striped bordered hover variant="light">
        <thead>{tableParts[0]}</thead> 
        <tbody>{tableParts[1]}</tbody>
        </Table>);
    }
    

    return(
        (typeof tableData.bodyContents === "string")
        ?
        <CreateTable 
            tableParts={[
                <CreateTableHeader contents={tableData.headerContents} />,
                <CreateBodyWithMsg str={tableData.bodyContents} />
            ]}
        />
        : 
        <CreateTable 
            tableParts={[
                <CreateTableHeader contents={tableData.headerContents} />,
                <CreateTableBody contents={tableData.bodyContents} />
            ]}
        />
    );
}

