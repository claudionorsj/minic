	.text
	.globl main
main:
	subq $8, %rsp
	movq $65, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq $1, %rdi
	cmpq $0, %rdi
	je L14
	movq $66, %rdi
	call putchar
L6:
	movq 0(%rsp), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
L14:
	movq $67, %rdi
	call putchar
	jmp L6
	.data
