import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    editInstance, editResourceOMIMEndpoint, editResourceSingleEndpoint,
    getEditRow,
    getOntologyStructure,
    getOntologyStructureInfo, removeInstance,
    storeEditRow
} from "./listResourcesSlice";
import BootstrapTable from 'react-bootstrap-table-next';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton
} from "@material-ui/core";
import {Col, Row} from "react-bootstrap";
import FormEditEntity from "./forms/FormEditEntity";
import {makeStyles} from "@material-ui/core/styles";
import FormEditConcept from "./forms/FormEditConcept";
import FormEditResource from "./forms/FormEditResource";
import {getLabels} from "../addSource/addSourceSlice";

let diff = require('object-diff');

const useStyles = makeStyles((theme) => ({
    button: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#1de9b6",
        color: "#1de9b6",
        '&:hover':{
            borderColor: "#1dc4e9",
            color: "#1dc4e9"
        },
    },
    buttonG: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#1de9b6",
        color: "#fff",
        '&:hover':{
            borderColor: "#1de9b6",
        },
        background: "linear-gradient(-135deg, #1de9b6 0%, #1dc4e9 100%)"
    }
}));


export const ListResources = () => {
    const dispatch = useDispatch();
    const ontologyStructure = useSelector(getOntologyStructure)
    const editRow = useSelector(getEditRow)
    const labels = useSelector(getLabels)
    const [open, setOpen] = useState(false);
    const [openRemove, setOpenRemove] = useState(false);

    const classes = useStyles();
    useEffect(() => {
        dispatch(getOntologyStructureInfo())
    }, [])

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleClickOpenRemove = () => {
        setOpenRemove(true);
    };

    const handleCloseRemove = () => {
        setOpenRemove(false);
    };

    const getNonExpandableRows = () => {
        let nonExpandableEntities = []
        let nonExpandableConcepts = []

        if (ontologyStructure.length !== 0) {
            ontologyStructure.map( (values) => {
                if(values.isEntityOf.length === 0) {
                    nonExpandableEntities.push(values.uri)
                }
                else {
                    values.isEntityOf.map( (conceptsExtended) => {
                        if (conceptsExtended !== null) {
                            if (conceptsExtended.hasResource.length === 0) {
                                nonExpandableConcepts.push(conceptsExtended.uri)
                            }
                        }
                    })
                }
            })
        }

        return [nonExpandableEntities,nonExpandableConcepts]
    }

    const GetActionFormat = (cell, row) => {
        return(<div style={{paddingTop: "3%"}}>
                <IconButton aria-label="upload picture" component="span" title="Edit" onClick={(e)=> {e.stopPropagation(); handleModelEdit(cell, row) }}>
                    <i className="feather icon-edit" style={{fontSize:"14px"}}></i>
                </IconButton>

                <IconButton aria-label="upload picture" component="span" title="Remove" onClick={(e)=> {e.stopPropagation(); handleModelRemove(cell, row) }}>
                    <i className="feather icon-x-square" style={{fontSize:"14px"}}></i>
                </IconButton>
            </div>
        )
    }

    const handleModelEdit = (cell, row) => {
        dispatch(storeEditRow(row))
        handleClickOpen();
    }

    const submitEdit = (values) => {
        let formData = new FormData()
        formData.append("uri", values.uri)
        formData.append("typeOf", values.typeOf)

        const d = diff(editRow, values)
        Object.entries(d).forEach(item => { formData.append(item[0], item[1]); })

        if ( 'isEndpointFile' in d ) {
            if ( values.publisher === "omim" ) {
                formData.append("genemap", values.genemap)
                formData.append("morbidmap", values.morbidmap)
                formData.append("label", values.label)
                dispatch(editResourceOMIMEndpoint(formData))
            }
            else {
                formData.append("file", values.files)
                formData.append("label", values.label)
                dispatch(editResourceSingleEndpoint(formData))
            }
        }
        else if ( Object.keys(d).length !== 0 ) {
            dispatch(editInstance(formData))
        }

        dispatch(getOntologyStructureInfo())
        setOpen(false)
    }

    const handleModelRemove = (cell, row) => {
        dispatch(storeEditRow(row))
        handleClickOpenRemove();
    }

    const removeInst = () => {
        let formData = new FormData()
        formData.append("uri", editRow.uri)
        formData.append("typeOf", editRow.typeOf)
        dispatch(removeInstance(formData))
        dispatch(getOntologyStructureInfo())
        console.log(ontologyStructure)
        setOpenRemove(false)
    }

    const editModal = (
        <Dialog open={open}  onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
            <DialogTitle  id="alert-dialog-title">Edit Resource "<i>{editRow.label}</i>"</DialogTitle>
            <DialogContent >
                <DialogContentText id="alert-dialog-description">
                    {editRow.typeOf === "Entity" && <FormEditEntity onSubmit={submitEdit} labels={labels}/> }
                    {editRow.typeOf === "Concept" && <FormEditConcept onSubmit={submitEdit} labels={labels}/> }
                    {editRow.typeOf === "Resource" && <FormEditResource onSubmit={submitEdit} labels={labels}/> }
                </DialogContentText>
            </DialogContent>
            <DialogActions style={{width: "98%", display: "block"}}>
                <div style={{ marginTop: '20px', marginBottom: "20px"}}>
                    <Row className="justify-content-md-center">
                        <Col sm="6" className="centerStuff">
                            <Button variant="outlined" color="primary" className={ classes.button } onClick={ handleClose }>
                                Cancel
                            </Button>
                        </Col>
                        <Col sm="6" className="centerStuff">
                            <Button variant="outlined" color="primary" className={ classes.buttonG } type="submit" form="editForm">
                                Submit
                            </Button>
                        </Col>
                    </Row>
                </div>
            </DialogActions>
        </Dialog>
    )

    const removeModal = (
        <Dialog open={openRemove}  onClose={handleCloseRemove} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
            <DialogTitle  id="alert-dialog-title">Are you sure you want to delete the instance "<i>{editRow.label}</i>"?</DialogTitle>
            <DialogContent >
                <DialogContentText id="alert-dialog-description">
                    Please note that when you delete an instance, you are removing the instances with which it is related.
                </DialogContentText>
            </DialogContent>
            <DialogActions style={{width: "98%", display: "block"}}>
                <div style={{ marginTop: '20px', marginBottom: "20px"}}>
                    <Row className="justify-content-md-center">
                        <Col sm="6" className="centerStuff">
                            <Button variant="outlined" color="primary" className={ classes.button } onClick={ handleCloseRemove }>
                                Cancel
                            </Button>
                        </Col>
                        <Col sm="6" className="centerStuff">
                            <Button variant="outlined" color="primary" className={ classes.buttonG } type="submit" onClick={removeInst}>
                                Confirm
                            </Button>
                        </Col>
                    </Row>
                </div>
            </DialogActions>
        </Dialog>
    )

    /*
        Table Expanded Rows
     */
    const columnsEntities = [
        {
            dataField: "title",
            text: "Entity Title",
            headerStyle: () => {
                return { width: "30.6%" };
            }
        },
        {
            dataField: "label",
            text: "Entity Label",
            sort: true,
            headerStyle: () => {
                return { width: "28.6%" };
            }
        },
        {
            dataField: "description",
            text: "Entity Description",
            headerStyle: () => {
                return { width: "47%" };
            }
        },
        {
            text : 'Action',
            dataField : '',
            formatter : GetActionFormat,
            classes : 'p-1',
            headerStyle: () => {
                return { width: "3%" };
            }
        }
    ];

    const columnsConcept = [
        {
            dataField: "title",
            text: "Concept Title",
            headerStyle: () => {
                return { width: "27.3%" };
            }
        },
        {
            dataField: "label",
            text: "Concept Label",
            sort: true,
            headerStyle: () => {
                return { width: "28%" };
            }
        },
        {
            dataField: "description",
            text: "Concept Description",
            headerStyle: () => {
                return { width: "42%" };
            }
        },
        {
            text : 'Action',
            dataField : '',
            formatter : GetActionFormat,
            classes : 'p-1',
            headerStyle: () => {
                return { width: "3%" };
            }
        }
    ];
    const expandRowConcept = {
        renderer: row => (
            <div style={{marginRight: "-12px", marginLeft: "1.7%", marginTop: "-1.05rem", paddingLeft: "-12px"}}>
                <BootstrapTable
                    keyField="uri"
                    data={row.isEntityOf}
                    columns={columnsConcept}
                    expandRow={expandRowResource}
                    hover
                />
            </div>

        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[0],
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
    };

    const columnsResource = [
        {
            dataField: "title",
            text: "Resource Title",
            headerStyle: () => {
                return { width: "13.35%" };
            }
        },
        {
            dataField: "label",
            text: "Resource Label",
            sort: true,
            headerStyle: () => {
                return { width: "13.35%" };
            }
        },
        {
            dataField: "description",
            text: "Resource Description",
            headerStyle: () => {
                return { width: "28.2%" };
            }
        },
        {
            dataField: "order",
            text: "R. Order",
            headerStyle: () => {
                return { width: "7%" };
            }
        },
        {
            dataField: "publisher",
            text: "R. Plugin",
            headerStyle: () => {
                return { width: "7%" };
            }
        },
        {
            dataField: "endpoint",
            text: "Resource Endpoint",
            headerStyle: () => {
                return { width: "40%" };
            }
        },
        {
            text : 'Action',
            dataField : '',
            formatter : GetActionFormat,
            classes : 'p-1',
            headerStyle: () => {
                return { width: "3%" };
            }
        }
    ];
    const expandRowResource = {
        renderer: row => (
            <div style={{marginRight: "-12px", marginLeft: "1.7%", marginTop: "-1.05rem", paddingLeft: "-12px"}}>
                <BootstrapTable
                    keyField="label"
                    data={row.hasResource}
                    columns={columnsResource}
                    hover
                    expandRow={expandRowParser}
                />
            </div>
        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[1],
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
    };

    const expandRowParser = {
        renderer: row => (
            <div>
                <p>You can render anything here, also you can add additional data on every row object</p>
            </div>
        ),
        showExpandColumn: true,
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
    };



    /*
        Return of component
     */
    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable
                keyField="uri"
                data={ontologyStructure}
                columns={columnsEntities}
                expandRow={expandRowConcept}
                hover
                headerClasses="header-class"
            />

            {editModal}
            {removeModal}
        </div>
    )
}