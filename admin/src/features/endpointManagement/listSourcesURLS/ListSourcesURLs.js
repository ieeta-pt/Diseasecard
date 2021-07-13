import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import BootstrapTable from 'react-bootstrap-table-next';
import {
    editSourceURL, getEditRow,
    getLabelsBaseURLS,
    getListSourcesURLS, getResourcesWithoutBaseURL,
    getSourcesURLS, removeSourceURL,
    storeEditRow
} from "../endpointManagementSlice";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton
} from "@material-ui/core";
import FormEditEntity from "../../sourcesManagement/listResources/forms/FormEditEntity";
import {Col, Row} from "react-bootstrap";
import {useStyles} from "../../sourcesManagement/addSource/forms/FormElements";
import FormEditSourceBaseURL from "./form/FormEditSourceBaseURL";

export const ListSourcesURLs = () => {
    const dispatch = useDispatch()
    const sourcesURLs = useSelector(getListSourcesURLS)
    const editRow = useSelector(getEditRow)
    const labels = useSelector(getLabelsBaseURLS)
    const [open, setOpen] = useState(false);
    const [openRemove, setOpenRemove] = useState(false);
    const classes = useStyles();

    useEffect(() => {
        dispatch(getSourcesURLS())
    }, [])


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
        console.log(row)
        dispatch(storeEditRow(row))
        handleOpen()
    }


    const handleModelRemove = (cell, row) => {
        console.log(row)
        dispatch(storeEditRow(row))
        handleOpenRemove()
    }


    const handleClose = () => {
        setOpen(false);
    }
    const handleOpen = () => {
        setOpen(true);
    };
    const handleCloseRemove = () => {
        setOpenRemove(false);
    };
    const handleOpenRemove = () => {
        setOpenRemove(true);
    };


    const submitEdit = async (values) => {
        let formData = new FormData()
        formData.append("resourceLabel", values.source)
        formData.append("baseURL", values.url)
        handleClose()

        await dispatch(editSourceURL(formData))
        dispatch(getResourcesWithoutBaseURL())
        dispatch(getSourcesURLS())
    }


    const removeSourceBaseURL = async () => {
        let formData = new FormData()
        formData.append("resourceLabel", editRow.source)
        handleCloseRemove()

        await dispatch(removeSourceURL(formData))
        dispatch(getResourcesWithoutBaseURL())
        dispatch(getSourcesURLS())
    }


    const editModal = (
        <Dialog open={open}  onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
            <DialogTitle  id="alert-dialog-title">Edit Source Base URL "<i>{editRow.source}</i>"</DialogTitle>
            <DialogContent >
                <DialogContentText id="alert-dialog-description">
                    <FormEditSourceBaseURL onSubmit={submitEdit} labels={labels}/>
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
                            <Button variant="outlined" color="primary" className={ classes.buttonG } type="submit" form="editSourceBaseURL">
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
            <DialogTitle  id="alert-dialog-title">Are you sure you want to delete the instance "<i>{editRow.source}</i>"?</DialogTitle>
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
                            <Button variant="outlined" color="primary" className={ classes.buttonG } type="submit" onClick={removeSourceBaseURL}>
                                Confirm
                            </Button>
                        </Col>
                    </Row>
                </div>
            </DialogActions>
        </Dialog>
    )


    const columns = [
        {
            dataField: 'source',
            text: 'Source',
            headerStyle: () => {
                return { width: "25%" };
            }
        },
        {
            dataField: 'url',
            text: 'Base URL'
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


    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable keyField='source' data={sourcesURLs} columns={columns} hover/>
            {editModal}
            {removeModal}
        </div>

    )
}