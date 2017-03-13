import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'filter'})
export class FilterPipe implements PipeTransform {
    transform(items: any[], prop: string,  value: string): any[] {  
      if (!items) return [];        
      
      return items.filter(it => it[prop] === value);
    }
}

