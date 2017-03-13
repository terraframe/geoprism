import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'keys',
  pure: false
})
export class KeysPipe implements PipeTransform {
  transform(value:any, args:string[]) : any {
    let keys = new Array<any>();
    for (let key in value) {
      keys.push({key: key, value: value[key]});
    }
    return keys;
  }
}
