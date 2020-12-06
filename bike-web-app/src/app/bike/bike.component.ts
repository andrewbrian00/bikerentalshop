import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables/src/angular-datatables.directive';
import { Subject } from 'rxjs';

declare interface DataTable {
  headerRow: string[];
}

declare const $: any;

@Component({
  selector: 'app-bike',
  templateUrl: './bike.component.html',
  styleUrls: ['./bike.component.scss']
})
export class BikeComponent implements AfterViewInit, OnInit, OnDestroy{

  title: String = "Search Bike";

  searchForm: FormGroup;
  
  public dataTable: DataTable;
  private response: any;
  public dealers: any = [];
  dtTrigger: Subject<any> = new Subject();
  dtOptions: DataTables.Settings = {};
  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;
  minLength: number = 7;
  maxLength: number = 7;


  //spinner
  color = 'warn';
  mode = 'indeterminate';
  value = 50;
  displayProgressSpinner = false;
  spinnerWithoutBackdrop = false;

  initDisabled: boolean = true;
  maxDate = new Date();

  //datatables
  elemMainPanel = <HTMLElement>document.querySelector('.main-panel');
  recordsTotal: number;
  recordsFiltered: number;
  sorting = {}
  cachedRow = [];

  constructor(
    private formBuilder: FormBuilder

    ) { }

  ngAfterViewInit(): void {
    throw new Error('Method not implemented.');
  }
  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit() {
    const self = this;
    this.searchForm = this.formBuilder.group({
      customerName: ['', Validators.pattern],
      checkOutTime: [''],
      checkInTime: [''],
      totalTimeSpent: [''],
      contactNo: ['']
    });

  }

  clearDate() : void{

  }

  search(): void{

  }


}
