import TextField from "@material-ui/core/TextField";
import React from "react";
import FormHelperText from "@material-ui/core/FormHelperText";
import {makeStyles} from "@material-ui/core/styles";
import DropZone from "react-dropzone";
import {Col, Row} from "react-bootstrap";
import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";


const renderFromHelper = ({ touched, error, labelText }) => {
    if (!(touched && error)) {
        return <FormHelperText style={{marginLeft: "2px"}} >{labelText}</FormHelperText>
    } else {
        return <FormHelperText>{touched && error}</FormHelperText>
    }
}


export const useStyles = makeStyles((theme) => ({
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
    },
    field: {
        borderColor: "#1dc4e9",
        '& label.Mui-focused': {
            color: "#1dc4e9",
        },
        '& .MuiInput-underline:after': {
            borderBottomColor: "#1dc4e9",
        },
        '& .MuiOutlinedInput-root': {
            '&:hover fieldset': {
                borderColor: "#1dc4e9",
            },
            '&.Mui-focused fieldset': {
                borderColor: "#1dc4e9",
            },
        }
    },
    switch: {
        borderColor: "#1de9b6",
        '& .MuiSwitch-colorPrimary.Mui-checked': {
            color: "#1de9b6",
        },
        '& .MuiSwitch-colorPrimary.Mui-checked + .MuiSwitch-track': {
            backgroundColor: "#1de9b6",
        }
    },
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(2),
            width: '100%',
        },
        '& .MuiButton-outlined.Mui-disabled': {
            background: "#748892"
        },
        '& .MuiButton-root.Mui-disabled': {
            color: "#fff"
        },
    },
}));


export const renderTextField = ({ label, input, labelText, meta: { touched, invalid, error },  ...custom }) => (
    <TextField
        label={label}
        placeholder={label}
        error={touched && invalid}
        helperText={renderFromHelper({ touched, error, labelText })}
        {...input}
        {...custom}
    />
)


export const renderSelectField = ({ input,  label, labelText, meta: { touched, invalid, error }, children, ...custom}) => (
    <TextField
        id="outlined-select-currency"
        select
        variant="outlined"
        label={label}
        placeholder={label}
        error={touched && invalid}
        helperText={renderFromHelper({ touched, error, labelText })}
        {...input}
        {...custom}
    >
        {children}
    </TextField>
)


export const renderDropzoneInput = (field) => {
    let f = field.input.value || null;
    const style = field.style
    return (
        <div>
            <DropZone
                onDrop={(filesToUpload) => {
                    filesToUpload.map((file) => {
                        f = file
                    });
                    field.input.onChange(f);
                }}
                maxFiles={1}>
                {({getRootProps, getInputProps}) => (
                    <section>
                        <div {...getRootProps({style})}>
                            <input {...getInputProps()} />
                            {f &&  <p style={{marginBottom: "0", color: "#888"}}><b>Selected File:</b>&nbsp;{f.name}</p>}
                            {f==null && <p style={{marginBottom: "0", color: "#888"}}>Drag 'n' drop a file here, or click to select one</p> }
                        </div>
                    </section>
                )}
            </DropZone>
            {field.meta.touched && field.meta.error && <span className="error">{field.meta.error}</span>}

        </div>
    );
}


export const FootForm = (props, c) => {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        if (props.valid) setOpen(true);
    };
    const goBack = () => {
        props.formDetails.goToStep(5);
    };
    const handleClose = () => {
        setOpen(false);
        props.formDetails.goToStep(5);
    };

    return (
        <div style={{ marginTop: '20px', marginBottom: "20px", width: "100%"}}>
            <Row className="justify-content-md-center">
                <Col sm="6" className="centerStuff">
                    <Button variant="outlined" color="primary" className={ c.buttonG } type="button" onClick={goBack}>
                        Go Back
                    </Button>
                </Col>
                <Col sm="6" className="centerStuff">
                    <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ props.pristine || props.submitting || props.invalid } onClick={handleClickOpen}>
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
        </div>
    )
}