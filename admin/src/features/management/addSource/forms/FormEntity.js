import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { Grid, MenuItem } from "@material-ui/core";
import {FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";
import {useSelector} from "react-redux";
import {getConceptsLabels} from "../addSourceSlice";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'titleEntity',
        'labelEntity',
        'descriptionEntity',
    ]
    requiredFields.forEach(field => {
        if (!values[field]) {
            errors[field] = 'Required'
        }
    })
    if ( values.email && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
        errors.email = 'Invalid email address'
    }
    return errors
}


const AddEntityFrom = props => {
    const { handleSubmit, classes} = props

    const c = useStyles();

    const conceptsLabels = useSelector(getConceptsLabels)

    return (
        <div style={{width: "94%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="addEntityForm">
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="titleEntity"
                            component={renderTextField}
                            label="Title"
                            className={c.field}
                            labelText="olaaa"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="labelEntity"
                            component={renderTextField}
                            label="Label"
                            className={c.field}
                            labelText="olaaa"
                        />
                    </Grid>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="descriptionEntity"
                            component={renderTextField}
                            label="Description"
                            className={c.field}
                            labelText="olaaa"
                            multiline
                            rows={2}
                        />
                    </Grid>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}  >
                        <Field
                            classes={classes}
                            size="small"
                            name="isEntityOfEntity"
                            component={renderSelectField}
                            label="Entity Of"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {conceptsLabels.map((option) => (
                                <MenuItem key={option} value={option}>
                                    {option}
                                </MenuItem>
                            ))}
                        </Field>
                    </Grid>

                    {FootForm(props, c)}

                    {/*<div style={{ marginTop: '20px', marginBottom: "20px", width: "100%"}}>
                        <Row className="justify-content-md-center">
                            <Col sm="6" className="centerStuff">
                                <Button variant="outlined" color="primary" className={ c.buttonG } type="button" onClick={goBack}>
                                    Go Back
                                </Button>
                            </Col>
                            <Col sm="6" className="centerStuff">
                                <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ pristine || submitting || invalid } onClick={handleClickOpen}>
                                    Submit
                                </Button>
                                <Dialog open={open} onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
                                    <DialogTitle id="alert-dialog-title">{"Use Google's location service?"}</DialogTitle>
                                    <DialogContent>
                                        <DialogContentText id="alert-dialog-description">
                                            Let Google help apps determine location. This means sending anonymous location data to
                                            Google, even when no apps are running.
                                        </DialogContentText>
                                    </DialogContent>
                                    <DialogActions style={{width: "100%", display: "block"}}>
                                        <div style={{ marginTop: '20px', marginBottom: "20px"}}>
                                            <Row className="justify-content-md-center">
                                                <Col sm="6" className="centerStuff">
                                                    <a href="#" className='label theme-bg text-white f-14' style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ handleClose }>Add More</a>
                                                </Col>
                                                <Col sm="6" className="centerStuff">
                                                    <a href="#" className={'label theme-bg text-white f-14'} style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} > Finish </a>
                                                </Col>
                                            </Row>
                                        </div>
                                    </DialogActions>
                                </Dialog>
                            </Col>
                        </Row>
                    </div>*/}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddEntityFrom', // a unique identifier for this form
    validate,
    asyncValidate
})(AddEntityFrom)
